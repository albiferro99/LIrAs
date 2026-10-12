package it.polimi.generator

import it.polimi.lIRAs.Model
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.List
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext
import it.polimi.lIRAs.ActionC

class LIRAsGenerator extends AbstractGenerator{
    override void doGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context) {
        val start = System.nanoTime
        
        resource.allContents.filter(Model).forEach[model |
            
            // Generate the <PatternName>.xml starting from the Template Pre-Gen.xml
            val xml = generateXML()
            val filePath = String.format("%s.xml", model.pattern.name.toString())
            fsa.generateFile(filePath, xml)
            
            // Channel declaration for each subsequences
            replace(filePath, "// **sub-sequences**", ssChan (model), fsa)
            
            // Declare the status for each Sequence (agent), by default is initially set to true
            // The status is used as guard in the Pattern Orchestrator
            replace(filePath, "// **agent_status**", sStatus (model), fsa)
            
            // Generate the declaration for the Locations (e.g. rooms)
            replace(filePath, "// **input_location**", iLocation (model), fsa)
            // Generate the declaration for the Resources (e.g. rooms)
            replace(filePath, "// **input_resource**", iResource (model), fsa)
            
            // Declare the number of Agent (both Robot and Humans) in the pattern
            replace(filePath, "// **human_number**", hNumber (model), fsa)
            replace(filePath, "// **robot_number**", rNumber (model), fsa)
            replace(filePath, "// **resource_number**", iNumber (model), fsa)
            
            // Generate the declarations to initialize a Human agent (fatigue, position)
            replace(filePath, "// **human_initialization**", hInit (model), fsa)
            
            // Generate the declarations to initialize a Robot agent (SoC, position)
            replace(filePath, "// **robot_initialization**", rInit (model), fsa)
           	
        	// DEFINE ALL THE ACTIONS AND THEIR PARAMETERS
        	injectActions(filePath, fsa, model)
        	
        	
        	// Generate the Pattern_Orchestrator
        	replace(filePath, "// **pattern_orchestrator**", pattOrch(model), fsa)
        	
        	// Generate the Sequence_Ochestrator
        	replace(filePath, "// **sequence_orchestrator**", seqOrch(model), fsa)

        	// Define the orchestrator
        	replace(filePath, "// **pattern_definition**", pattDef (model), fsa)
        	
        	// Declare the orchestrator
        	replace(filePath, "// **pattern_dichiaration**", pattDic (model), fsa)
        	
        	// Generate the verifier
        	replace(filePath, "// **verifier**", verifier(model), fsa)
        ]
        val end = System.nanoTime
    	val elapsedMs = (end - start) / 1_000_000
    	println('''Generation completed in «elapsedMs» ms''')
    }

	// Generate the .xml file calling the function that read the template as starting point 
    def String generateXML () {
        '''
    «readTemplate()»
        '''
    }

    // Read the basic template file
    def String readTemplate () {
        try{
            val filePath = "/Users/alberto_tagliaferro/Library/CloudStorage/OneDrive-PolitecnicodiMilano/Desktop/LIRAs_Generation/runtime-EclipseApplication/src-gen/Pre_Gen.xml"
            val lines = Files.readAllLines(Paths.get(filePath))
            return lines.join("\n")
        }catch(IOException e) {
            e.printStackTrace
            return "Error reading file"
        }
    }

    // Define the function that read the xml and substitute a string with another one
    def void replace (String inputFileName, String targetString, String newString, IFileSystemAccess2 fsa) {
        try{
            // Read the input file
            val fileContent = fsa.readTextFile(inputFileName)
            
            // Convert into a string
            if (fileContent !== null) {
                val updatedContent = fileContent.toString.replace(targetString, newString)
                // override the file with the new string
                fsa.generateFile(inputFileName, updatedContent)
            }
        }catch(Exception e) {
            e.printStackTrace
        }
    }
    
    // Function that compute the maximum subSequence value and declare the necessary Urgent Channel
	def String ssChan(Model model) {
	    // 1. Find the maximum value between i and j across all subsequences (with fallback to 0)
	    val maxSSnum = model.sequence
	        .flatMap[subSequence]
	        .map[Math.max(i, j)]
	        .max ?: 0
	
	    // If there are no subsequences, declare only the 'e' channel
	    if (maxSSnum == 0) {
	        return "urgent broadcast chan e;"
	    }
	
	    // 2. Create the list of strings "s1", "s2", etc. and join them with a comma
	    val channels = (1..maxSSnum).map["s" + it].join(", ")
	
	    // 3. Use Template Expressions to compose the final result
	    return '''urgent broadcast chan «channels», e;'''.toString
	}
    
    // Function that declare the initial status of each Sequence as true
	def String sStatus(Model model) {
	    // Map each agent's name to its boolean status declaration and join them with a newline
	    return model.sequence
	        .map["bool " + agent.name.toLowerCase + "_stat = true;"]
	        .join("\n") + "\n"
	}

	// Function that generate the locations array for the input location AND resources
	def String iLocation(Model model) {
	    // 1. Safe retrieval of lists
	    val locations = model.pattern.location ?: emptyList
	    val resources = model.pattern.resources ?: emptyList
	    
	    val totalSize = locations.size + resources.size
	    
	    // 2. Safety check
	    if (totalSize == 0) {
	        return "// No locations or resources declared. Using dummy to prevent compilation errors.\n"
	    }
	
	    // 3. Map safely with strong typing, THEN combine the resulting iterables of strings
	    val stringX = (locations.map[name + "[0]"] + resources.map[name + "[0]"]).join(", ")
	    val stringY = (locations.map[name + "[1]"] + resources.map[name + "[1]"]).join(", ")
	    
	    return '''
double location_X[«totalSize»] = {«stringX»};
double location_Y[«totalSize»] = {«stringY»};
	    '''.toString
	}

	// Function that generates the arrays for the input resources
	def String iResource(Model model) {
	    val resources = model.pattern.resources
	
	    // Safety check: if the list is null or empty, return a comment
	    if (resources.nullOrEmpty) { return "// No resources in pattern\n" }
	
	    // Generate the arrays mapping X and Y coordinates directly
	    return '''
double resources_X[«resources.size»] = {«resources.map[name + "[0]"].join(", ")»};
double resources_Y[«resources.size»] = {«resources.map[name + "[1]"].join(", ")»};
	    '''.toString
	}

	// Function that declares the number of humans in the pattern, if any
	// (n.b. an agent that is not used for a SubSequence can still be needed for the conditions if passed as input, so it has to be fully defined)
	def String hNumber(Model model) {
	    val hNum = model.pattern.human.size
	    
	    // Return the declaration or a comment if no humans are present
	    if (hNum > 0) {
	        '''const int N_H = «hNum»; // Number of humans in the pattern'''.toString
	    } else {
	        "// There are no Humans in the pattern Input"
	    }
	}
	
	// Function that declares the number of robots in the pattern, if any
	def String rNumber(Model model) {
	    val rNum = model.pattern.robot.size
	    if (rNum > 0) { '''const int N_R = «rNum»; // Number of robots in the pattern'''.toString
	    } else { "// There are no Robots in the pattern Input" }
	}
	
	// Function that declares the number of resources in the pattern, if any
	def String iNumber(Model model) {
	    val iNum = model.pattern.resources.size
	    if (iNum > 0) { '''const int N_ITEMS = «iNum»; // Number of resources in the pattern'''.toString
	    } else { "// There are no Resources in the pattern Input" }
	}
	
	// Function that initializes the variables necessary for a Human agent
	def String hInit(Model model) {
	    val hNum = model.pattern.human.size
	    
	    // If no humans, declare dummy arrays to prevent compilation errors
	    if (hNum == 0) {
	        return '''
	        // No humans declared: declare dummy arrays to prevent compilation errors
	        double humanPositionX[1] = {0.0};
	        double humanPositionY[1] = {0.0};
	        '''.toString
	    }
	
	    // Generate a single string of "0.0" repeated hNum times, separated by comma
	    val zeros = (1..hNum).map["0.0"].join(", ")
	    val nodeIndices = (0..<hNum).map[it.toString].join(", ")
	    
	    // Use Template Expressions to compose the declarations
	    return '''
	    // Human initialization
	    double humanFatigue[N_H] = {«zeros»};
	    // The humans will start from the first N_H intersections
	    const int H_start_nodes[N_H] = {«nodeIndices»};
	    double humanPositionX[N_H] = {«zeros»};
	    double humanPositionY[N_H] = {«zeros»};
	    '''.toString
	}
	
	
	// Function that initializes the variables necessary for a Robot agent
	def String rInit(Model model) {
	    val rNum = model.pattern.robot.size
	    
	    // Return a comment if no input Robot has been declared
	    if (rNum == 0) return "// No input Robot has been declared\n"
	
	    // Generate strings for repeated values
	    val charges = (1..rNum).map["100.0"].join(", ")
	    val zeros = (1..rNum).map["0.0"].join(", ")
	    val nodeIndices = (0..<rNum).map[it.toString].join(", ")
	    
	    return '''
	    // Robot initialization
	    double batteryCharge[N_R] = {«charges»};
	    // The robots will start from the first N_R intersections
	    const int R_start_nodes[N_R] = {«nodeIndices»};
	    double robPositionX[N_R] = {«zeros»};
	    double robPositionY[N_R] = {«zeros»};
	    '''.toString
	}
	
	
	// ACTION CONFIGURATION
	// 1. Define the configuration structure for an action (must be static if nested)
	// To define a new action modify at point 2
	static class ActionConfig {
	    public val String agentType
	    public val String actionName
	    public val String templateFileName
	    public val String syncChannels
	    public val String variables
	    public val String missionDefFormat
	    public val String missionDecPrefix
	    public val boolean requiresLocationsOrResources
	    public val boolean requiresResourcesOnly
	
	    new(String agentType, String actionName, String templateFileName, String syncChannels, String variables, String missionDefFormat, String missionDecPrefix, boolean requiresLocationsOrResources, boolean requiresResourcesOnly) {
	        this.agentType = agentType
	        this.actionName = actionName
	        this.templateFileName = templateFileName
	        this.syncChannels = syncChannels
	        this.variables = variables
	        this.missionDefFormat = missionDefFormat
	        this.missionDecPrefix = missionDecPrefix
	        this.requiresLocationsOrResources = requiresLocationsOrResources
	        this.requiresResourcesOnly = requiresResourcesOnly
	    }
	}
	// 2. Function that returns the registry of all available actions
	def List<ActionConfig> getActionRegistry() {
	    return #[
	        // Human Idle
	        new ActionConfig("HUMAN", "idle", "Human_idle.xml",
	            "urgent broadcast chan obey_h_moveTo[N_H];",
	            "",
	            "hi_%d = Human_idle(%d);", "hi_", false, false),
	
	        // Human MoveTo
	        new ActionConfig("HUMAN", "moveTo", "Human_moveTo.xml",
	            "urgent broadcast chan start_h_moveTo[N_H], stop_h_moveTo[N_H];\nbroadcast chan hm_fail;",
	            "int human_loc_target[N_H];",
	            "hm_%d = Human_moveTo(%d, 26.0);", "hm_", true, false),
	
	        // Robot MoveTo
	        new ActionConfig("ROBOT", "moveTo", "Robot_moveTo.xml",
	            "urgent broadcast chan start_r_moveTo[N_R], stop_r_moveTo[N_R];\nbroadcast chan rm_still;",
	            "int robot_loc_target[N_R];",
	            "rm_%d = Robot_moveTo(%d, 26.0, 26.0);", "rm_", true, false),
	
	        // Robot Follow
	        new ActionConfig("ROBOT", "follow", "Robot_follow.xml",
	            "urgent broadcast chan start_r_follow[N_R], stop_r_follow[N_R];\nbroadcast chan rf_still;",
	            "// Who I have to follow (agent id)?\nint human_to_be_followed[N_R];\nint robot_to_be_followed[N_R];\nint f_loc_target[N_R];",
	            "rf_%d = Robot_follow(%d, 26.0, 26.0);", "rf_", false, false),
	
	        // Robot Pick
	        new ActionConfig("ROBOT", "pick", "Robot_pick.xml",
	            "urgent broadcast chan start_r_pick[N_R], stop_r_pick[N_R];",
	            "int robot_res_to_pick[N_R];\nint robot_inventory[N_R][N_ITEMS];\nint old_inventory[N_R][N_ITEMS];",
	            "rp_%d = Robot_pick(%d);", "rp_", false, true),
	
	        // Robot Deposit
	        new ActionConfig("ROBOT", "deposit", "Robot_deposit.xml",
	            "urgent broadcast chan start_r_deposit[N_R], stop_r_deposit[N_R];",
	            "int robot_res_to_deposit[N_R];",
	            "rd_%d = Robot_deposit(%d);", "rd_", false, true)
	            
	        // TO ADD A NEW ACTION JUST MODIFY HERE (+ add the xml template in the folder)
	        // EXAMPLE NEW ACTION: Robot Scan
	        //new ActionConfig(
	        //    "ROBOT",                                  // Agent type
	        //    "scan",                                   // Action name
	        //    "Robot_scan.xml",                         // Template file name
	        //    "urgent broadcast chan start_r_scan[N_R], stop_r_scan[N_R];", // Sync channels
	        //    "int robot_scan_target[N_R];",            // Variables needed
	        //    "rs_%d = Robot_scan(%d);",                // Mission definition format
	        //    "rs_",                                    // Mission declaration prefix
	        //    false,                                    // Requires locations/resources?
	        //    false                                     // Requires resources only?
	        //)
	    ]
	}
	// 3. Main function to inject all required actions into the XML template
	def void injectActions(String filePath, IFileSystemAccess2 fsa, Model model) {
	    val hNum = model.pattern.human.size
	    val rNum = model.pattern.robot.size
	    val lNum = model.pattern.location.size
	    val resNum = model.pattern.resources.size
	
	    val hasLocationsOrResources = lNum > 0 || resNum > 0 // Check if input loc/res in the pattern
	    val hasResources = resNum > 0
	
	    // Accumulators for the different parts of the XML
	    val syncBuilder = new StringBuilder
	    val varBuilder = new StringBuilder
	    val templateBuilder = new StringBuilder
	    val missDefBuilder = new StringBuilder
	    val missDecBuilder = new StringBuilder
	
	    for (config : getActionRegistry()) {
	        // Check if the action is needed based on agent types and resource requirements
	        val agentCount = if (config.agentType == "HUMAN") hNum else rNum
	        
	        val agentConditionMet = agentCount > 0
	        val locResConditionMet = !config.requiresLocationsOrResources || hasLocationsOrResources
	        val resConditionMet = !config.requiresResourcesOnly || hasResources
	
	        if (agentConditionMet && locResConditionMet && resConditionMet) {
	            
	            // Append Synchronizations and Variables
	            if (!config.syncChannels.nullOrEmpty) syncBuilder.append(config.syncChannels).append("\n")
	            if (!config.variables.nullOrEmpty) varBuilder.append(config.variables).append("\n")
	
	            // Append the XML Template content
	            templateBuilder.append(readActionTemplate(config.templateFileName)).append("\n")
	
	            // Append Mission Definitions
	            val defs = (1..agentCount).map[String.format(config.missionDefFormat, it, it)].join("\n")
	            missDefBuilder.append(defs).append("\n")
	
	            // Append Mission Declarations
	            val decs = (1..agentCount).map[config.missionDecPrefix + it].join(",\n")
	            missDecBuilder.append(decs).append(",\n")
	        }
	    }
	
	    // Replace the generic placeholders in the main file
	    replace(filePath, "// **actions_sync_channels**", syncBuilder.toString, fsa)
	    replace(filePath, "// **actions_variables**", varBuilder.toString, fsa)
	    replace(filePath, "// **actions_templates**", templateBuilder.toString, fsa)
	    replace(filePath, "// **actions_mission_definitions**", missDefBuilder.toString, fsa)
	    replace(filePath, "// **actions_mission_declarations**", missDecBuilder.toString, fsa)
	}
	// 4. Centralized function to read the XML action templates
	def String readActionTemplate(String fileName) {
	    val basePath = "/Users/alberto_tagliaferro/Library/CloudStorage/OneDrive-PolitecnicodiMilano/Desktop/LIRAs_Generation/runtime-EclipseApplication/src-gen/"
	    val fullPath = basePath + fileName
	    
	    try {
	        return Files.readAllLines(Paths.get(fullPath)).join("\n")
	    } catch (Exception e) {
	        e.printStackTrace
	        return ""
	    }
	}
	
	
	// Definition for the Orchestrators (Pattern and Sequence)
	def String pattDef(Model model) {
	    val patternName = model.pattern.name.toString()
	    val builder = new StringBuilder()
	    
	    // 1. Declare the main orchestrator
	    builder.append('''«patternName» = «patternName»_orchestrator(1);''').append("\n")
	    
	    // 2. We can separate human and robot sequences to manage their indices independently
	    val humanNames = model.pattern.human.map[name].toSet
	    val robotNames = model.pattern.robot.map[name].toSet
	
	    // We keep basic counters for clarity instead of array hacks
	    var hIndex = 1
	    var rIndex = 1
	
	    for (sequence : model.sequence) {
	        val agentName = sequence.agent.name
	        val agentLower = agentName.toLowerCase()
	        
	        if (humanNames.contains(agentName)) {
	            builder.append('''«patternName»_«agentLower»_def = «patternName»_«agentLower»(1, «hIndex»);''').append("\n")
	            hIndex++
	        } else if (robotNames.contains(agentName)) {
	            builder.append('''«patternName»_«agentLower»_def = «patternName»_«agentLower»(1, «rIndex»);''').append("\n")
	            rIndex++
	        }
	    }
	    
	    return builder.toString()
	}
	
	// Declaration for the Orchestrators (Pattern and Sequence)
	def String pattDic(Model model) {
	    val patternName = model.pattern.name.toString()
	    
	    // Create the main orchestrator string
	    val mainOrch = patternName
	    
	    // Create the list of definitions for each sequence
	    val seqDefs = model.sequence.map[
	        val agentLower = agent.name.toLowerCase()
	        '''«patternName»_«agentLower»_def'''
	    ]
	    
	    // Combine them and join with ",\n"
	    val allDeclarations = newArrayList(mainOrch)
	    allDeclarations.addAll(seqDefs)
	    
	    return allDeclarations.join(",\n")
	}
	
	
	// ==========================
	// PATTERN ORCHESTRATOR
	// ==========================
	
	def String pattOrch(Model model) {
		val patternName = model.pattern.name.toString()
		val builder = new StringBuilder()
		val xOffset = 500
		val yOffset = 0
		// Variables for locations parameters
		var String locName = null
		var String invariant = null
		var String  rate_of_exp = null
		var boolean urgent = false
		var boolean committed = false
		// Variables for transitions parameters
		var int source = 0
		var int target = 0
		var String select = null
		var String sync = null
		var boolean controllable = true
		var int nailX = 0
		var int nailY = 0
			
		builder.append("\t<template>\n")
		builder.append(String.format("\t\t<name>%s_orchestrator</name>\n", patternName))
		builder.append("\t\t<parameter>int p</parameter>\n")
		builder.append("\t\t<declaration>// Place local declarations here.\nclock end_c;\n\t\t</declaration>\n")

		// Calculate maximum subsequence value
		val maxIndexHelper = model.sequence
			.flatMap[subSequence]
			.map[Math.max(i, j)]
			.max ?: 0
		
		// GENERATE LOCATIONS
		var locId = 0
		
		// Initial Location
		locName = "Init"
		invariant = null
		rate_of_exp = null
		urgent = false
		committed = false
		builder.append(genLoc(locId, 0*xOffset, yOffset, locName, invariant, rate_of_exp, urgent, committed))
		locId++
			
		// Subsequence Locations, pre_end and end Location
		for (i : 1 .. maxIndexHelper + 2) {
		    locName = if (i <= maxIndexHelper) {
		        String.format("sub_seq_s%d", i)
		    } else if (i == maxIndexHelper + 1) {
		        "pre_end"
		    } else {
		        "end"
		    }
		
		    invariant = if (i == maxIndexHelper + 1) {
		        "end_c &lt;= 2"
		    } else {
		        null
		    }
		
		    rate_of_exp = null
		    urgent = false
		    committed = false
		
		    builder.append(genLoc(locId, i * xOffset, yOffset, locName, invariant, rate_of_exp, urgent, committed))
		    locId++
		}

		// Define the initial state of the automa
		builder.append("\t\t<init ref=\"id0\"/>\n")
		
		// GENERATE TRANSITIONS
		for (i : 0 .. maxIndexHelper - 1) {
			
			// Using full java.util path to prevent Xtend import/parsing errors
			val agentsToSync = new java.util.ArrayList<String>()
			
			// search for continuation
			for (sequence : model.sequence) {
				var hasContinuation = false
				
				for (subSeq : sequence.subSequence) {
					// Continuation is 1 for k in (subSeq.i + 1)..(subSeq.j)
					if (subSeq.j - subSeq.i > 0) {
						if (i >= subSeq.i && i < subSeq.j) {
							hasContinuation = true
						}
					}
				}
				
				// If continuation == 0 in original code
				if (!hasContinuation) {
					agentsToSync.add(sequence.agent.name.toLowerCase())
				}
			}

			// Build guards
			val guardsList = new java.util.ArrayList<String>()
			for (agent : agentsToSync) {
				guardsList.add(agent + "_stat==true")
			}
			if (i == 0) {
				guardsList.add("p==currP")
			}
			val guardStr = guardsList.join(" and ")
			
			// Build updates
			val updateStr = agentsToSync.map[it + "_stat=false"].join(", ")
			
			// Add the standard transition (guards and update build above)
			source = i
			target = i + 1
			select = null
			sync = String.format("s%d!", i + 1)
			controllable = true
			nailX = 0
			nailY = 0
			builder.append(genTran(locId, source, target, select, guardStr, sync, updateStr, controllable, xOffset, yOffset, nailX, nailY))
			locId++
			
			// Handle the transition to 'pre_end' location
			if (i == maxIndexHelper - 1) {
				val finalGuards = model.sequence.map[agent.name.toLowerCase() + "_stat==true"].join(" and ")
				val finalUpdate = "end_c=0"
				
				source = i + 1 
				target = i + 2
				select = null
				sync = "e!"
				controllable = true
				nailX = 0
				nailY = 0
				builder.append(genTran(locId, source, target, select, finalGuards, sync, finalUpdate, controllable, xOffset, yOffset, nailX, nailY))
				locId++	
			}
		}
		
		// Transition from pre_end to end
		source = maxIndexHelper + 1
		target = maxIndexHelper + 2
		select = null
		sync = null
		controllable = true
		nailX = 0
		nailY = 0
		
		val endGuard = "end_c &gt;= 2"
		val endUpdate = "currP=currP+1"
		
		builder.append(genTran(locId, source, target, select, endGuard, sync, endUpdate, controllable, xOffset, yOffset, nailX, nailY))
		locId++

		builder.append("\t</template>\n")
		return builder.toString()
	}
	

	// ==========================
	// SEQUENCE ORCHESTRATOR
	// ==========================	
	
	static class SeqActionConfig {
		public val String actionName
		public val String agentType
		public val String startSync
		public val String stopSync
		public val boolean hasInitialUpdate
		public val boolean hasBusyLoop
		
		new(String actionName, String agentType, String startSync, String stopSync, boolean hasInitialUpdate, boolean hasBusyLoop) {
			this.actionName = actionName
			this.agentType = agentType
			this.startSync = startSync
			this.stopSync = stopSync
			this.hasInitialUpdate = hasInitialUpdate
			this.hasBusyLoop = hasBusyLoop
		}
	}
	
	def List<SeqActionConfig> getSeqActionRegistry() {
		return #[
			new SeqActionConfig("moveTo", "human", "start_h_moveTo[H-1]!", "stop_h_moveTo[H-1]!", true, true),
			new SeqActionConfig("moveTo", "rob",   "start_r_moveTo[R-1]!", "stop_r_moveTo[R-1]!", true, false),
			new SeqActionConfig("follow", "rob",   "start_r_follow[R-1]!", "stop_r_follow[R-1]!", true, false),
			new SeqActionConfig("pick",   "rob",   "start_r_pick[R-1]!", "stop_r_pick[R-1]!", true, false),
			new SeqActionConfig("deposit","rob",   "start_r_deposit[R-1]!", "stop_r_deposit[R-1]!", true, false)
		]
	}
	
	def SeqActionConfig getSeqActionConfig(String actionName, String agentPrefix) {
		return getSeqActionRegistry().findFirst[
			actionName == it.actionName && agentPrefix == it.agentType
		]
	}
	
	// Return the next action in the list, or null if this is the last one
	def ActionC getNextAction(org.eclipse.emf.common.util.EList<ActionC> actions, ActionC currentAction) {
		val index = actions.indexOf(currentAction)
		if (index != -1 && index < actions.size - 1) {
			return actions.get(index + 1)
		}
		return null
	}
	
	def seqOrch (Model model) {
		// Declaration
		val StringBuilder transition = new StringBuilder
		val StringBuilder builder = new StringBuilder
		val StringBuilder guard = new StringBuilder
		val StringBuilder update = new StringBuilder
		var String locName = null
		var String invariant = null
		var String  rate_of_exp = null
		var boolean urgent = false
		var boolean committed = false
		// Variables for transitions parameters
		var int source = 0
		var int target = 0
		var String select = null
		var String sync = null
		var boolean controllable = true
		var int nailX = 0
		var int nailY = 0
		
		
		val int xOffset = 200
		val int yOffset = 350
		var int actionCount = 0
	
		// Calculate the highest subSequence value
		val maxIndex = model.sequence
			.flatMap[subSequence]
			.map[Math.max(i, j)]
			.max ?: 0
	    
	    
		// AgentID is incremented across sequences, so it stays outside the loop
		var AgentID = 0

		for (sequence : model.sequence) {
			AgentID++
			
			// Local variables for this specific sequence
			var id = 0
			var x = 0
			var y = 0
			var actionID = 1
			var int endID = 0
			
			// Arrays declared inside the loop guarantee a clean slate for each sequence 
			val continuation = newIntArrayOfSize(maxIndex)		
			val emptySS = newIntArrayOfSize(maxIndex)
			val firstActionID = newIntArrayOfSize(maxIndex + 1) // id of the first action for each subsequence
			val lastActionID = newIntArrayOfSize(maxIndex + 1)  // id of the last action for each subsequence
			val endSubSeqID = newIntArrayOfSize(maxIndex + 1)   // id of the end subsequence location
			
			// Search for continuations
			for (subSequence : sequence.subSequence) {
				if (subSequence.j - subSequence.i > 0) {
					for (j : subSequence.i + 1 .. subSequence.j) {
						continuation.set(j - 1, 1)	
					}
				}
			}
			
	        // ==========================================================
	        // ********* SEQUENCE ORCHESTRATOR LOCATIONS ***************
			// ==========================================================
	        
	        // Determine the agent type once to reuse it later
			val agentName = sequence.agent.name.toString()
			val isHuman = model.pattern.human.map[name.toString()].toList.contains(agentName)
			val isRobot = model.pattern.robot.map[name.toString()].toList.contains(agentName)
			
			// Set the parameter for the orch. accordingly to the agent type
			builder.append('''
				<template>
					<name>«model.pattern.name»_«agentName.toLowerCase()»</name>
					«IF isHuman»
					<parameter>int p, int H</parameter>
					«ENDIF»
					«IF isRobot»
					<parameter>int p, int R</parameter>
					«ENDIF»
					<declaration>// Place local declarations here.
				clock t;
					</declaration>
			''')
	        
	        // Initial location for the sequence
	        locName = '''init_«agentName.toLowerCase()»'''.toString
			invariant = null
			rate_of_exp = null
			urgent = false
			committed = false
	        builder.append(genLoc(id, x, y, locName, invariant, rate_of_exp, urgent, committed))
			id++
			
	        // Initial location for each subSequence
			for (i : 1..maxIndex) {
				y += yOffset
				x = 0
				
				if (continuation.get(i - 1) == 0) {
					locName = '''s«i»_start'''.toString()
					invariant = null
					rate_of_exp = null
					urgent = false
					committed = true
					builder.append(genLoc(id, x, y, locName, invariant, rate_of_exp, urgent, committed))
				}
				id++            		
				continuation.set(i - 1, 0)
				
				if (i == maxIndex) {
					y += yOffset
					x = 0
					
					locName = '''end_«agentName.toLowerCase()»'''.toString()
					invariant = null
					rate_of_exp = null
					urgent = false
					committed = false
					builder.append(genLoc(id, x, y, locName, invariant, rate_of_exp, urgent, committed))
					
					endID = id
					id++
				}
			}
	         
	        y = 0
			for (subSequence : sequence.subSequence) {
				// In case of continuation we don't want idle locations (except for an empty continuation)
				if (subSequence.j != 0) {
					for (i : subSequence.i + 1 .. subSequence.j) {
						emptySS.set(i - 1, 1)
					}
				}
				
				x = 0
				y = subSequence.i * yOffset

				for (actionC : subSequence.actionC) {
					emptySS.set(subSequence.i - 1, 1) // The subSequence is not empty
					
					val aType = actionC.actionType.toString()
					
					// Action busy locations
					x += xOffset
					
					// The agent doing the action is a Human? => Put the invariant t <= 5 for the start iteration
					locName = '''A«AgentID»_a«actionID»_«aType»_busy'''.toString()
					invariant = if (isHuman) "t&lt;=5" else null
					rate_of_exp = null
					urgent = false
					committed = false
					builder.append(genLoc(id, x, y, locName, invariant, rate_of_exp, urgent, committed))
					
					// Save the id of the first busy action for each SubSequence
					if (x == xOffset) {
						firstActionID.set(subSequence.i, id)
					}
					id++
					
					// Action complete
					x += xOffset
					locName = '''A«AgentID»_a«actionID»_«aType»_complete'''.toString()
					invariant = null
					rate_of_exp = null
					urgent = false
					committed = true
					builder.append(genLoc(id, x, y, locName, invariant, rate_of_exp, urgent, committed))
					
					lastActionID.set(subSequence.i, id)
					id++
					actionID++
				}
				
				// ==============
				// Location and transition for the conditional statement
				// ==============
				actionCount = 0
				for (actionC : subSequence.actionC) {
				
					actionCount++
				
					// Check conditional statements and generate guard when present
					if (!actionC.conditionalStatement.isEmpty) {
				
						for (cs : actionC.conditionalStatement) {
				
							// Build guard from atomic predicates of the conditional statement
							for (c : cs.condition) {
				
								// === FIRST ATOMIC PREDICATE ===
								val pred0 = c.atomicPredicate.get(0)
				
								if (pred0.ap == 'position') {
									val baseGuard = apPositionGuard(
										model,
										pred0.agent.name.toString(),
										pred0.location.name.toString()
									)
				
									if (pred0.negated) {
										guard.append("not (" + baseGuard + ")")
									} else {
										guard.append("(" + baseGuard + ")")
									}
								}
				
								// === ADDITIONAL ATOMIC PREDICATES (AND / OR) ===
								if (c.atomicPredicate.size > 1) {
									for (i : 0 ..< (c.atomicPredicate.size - 1)) {
										val predNext = c.atomicPredicate.get(i + 1)
										val operator = c.op.get(i).toString()
				
										// Append logical operator
										if (operator == '&&') {
											guard.append(" and(")
										} else if (operator == '||') {
											guard.append(" or(")
										}
				
										// Build predicate guard
										val nextGuard = apPositionGuard(
											model,
											predNext.agent.name.toString(),
											predNext.location.name.toString()
										)
				
										// Append predicate with optional negation
										if (predNext.negated) {
											guard.append("\n\tnot (" + nextGuard + "))")
										} else {
											guard.append("\n\t(" + nextGuard + "))")
										}
									}
								}
							}
				
							// Create the synchronization according to the conditioned action
							if (isHuman) {
								sync = '''_h_«actionC.actionType.toString()»[H-1]!'''.toString
							} else if (isRobot) {
								sync = '''_r_«actionC.actionType.toString()»[R-1]!'''.toString
							}
				
							// Create the update according to the original action
							if (isHuman) {
								update.append(getActionUpdate(model, actionC, "human", "H-1"))
							} else if (isRobot) {
								update.append(getActionUpdate(model, actionC, "rob", "R-1"))
							}
							
							if (cs.cs1 !== null) { // until
								// Add the transition from the current action busy location to its complete location
								source = firstActionID.get(subSequence.i) + actionCount * 2 - 2
								target = firstActionID.get(subSequence.i) + actionCount * 2 - 1
								select = null
								
								controllable = true
								x = xOffset * (actionCount * 2 - 2) + xOffset
								y = subSequence.i * yOffset - 100
								nailX = xOffset * (actionCount * 2 - 2) + xOffset + 100
								nailY = subSequence.i * yOffset - 100
								transition.append(genTran(id, source, target, select, guard.toString(), "stop" + sync, null, controllable, x, y, nailX, nailY))
								id++
							}
							
							if (cs.cs2 !== null) { // if/else
							
								// Check the type of the alternative action
								// =================== Stop ===================
								if (cs.action.actionType.toString() == "stop") {
							
									// Generate the location for the stop alternative action
									locName = String.format("A%d_a%d_%s", AgentID, actionID, cs.action.actionType.toString())
									invariant = null
									rate_of_exp = null
									urgent = false
									committed = false
									x = actionCount * xOffset * 2 - xOffset
									y = subSequence.i * yOffset - (yOffset / 2)
									builder.append(genLoc(id, x, y, locName, invariant, rate_of_exp, urgent, committed))
									id++
									actionID++
							
									// Add the transition from the current action to the alternative action
									source = firstActionID.get(subSequence.i) + actionCount * 2 - 2
									target = id - 1
									select = null
									controllable = true
									x = xOffset * actionCount * 2 + xOffset
									y = subSequence.i * yOffset - 100
									nailX = 0
									nailY = 0
									transition.append(genTran(id, source, target, select, "not (" + guard.toString() + ")", "stop" + sync, null, controllable, x, y, nailX, nailY))
									id++
							
									// Add the transition from the alternative action to the correct target
									if (actionCount == 1) {
										source = id - 2
										target = subSequence.i
										select = null
										sync = "stop" + sync
										controllable = true
										x = xOffset * actionCount * 2 + xOffset
										y = subSequence.i * yOffset - 100
										nailX = 0
										nailY = 0
										transition.append(genTran(id, source, target, select, guard.toString(), sync, update.toString(), controllable, x, y, nailX, nailY))
										id++
									} else {
										source = id - 2
										target = firstActionID.get(subSequence.i) + actionCount * 2 - 3
										select = null
										sync = "stop" + sync
										controllable = true
										x = xOffset * actionCount * 2 + xOffset
										y = subSequence.i * yOffset - 100
										nailX = 0
										nailY = 0
										transition.append(genTran(id, source, target, select, guard.toString(), sync, update.toString(), controllable, x, y, nailX, nailY))
										id++
									}
								}
							
								// =================== MoveTo ===================
								if (cs.action.actionType.toString() == "moveTo") {
							
								}
							}
							
							if (cs.cs3 !== null) { // if/else/until
								locName = String.format("A%d_a%d_%s", AgentID, actionID, cs.action.actionType.toString())
								invariant = null
								rate_of_exp = null
								urgent = false
								committed = false
								builder.append(genLoc(id, x - xOffset, y - (yOffset / 2), locName, invariant, rate_of_exp, urgent, committed))
								id++
								actionID++
							}
							
							if (cs.cs4 !== null) { // wait
							
							}
				            guard.setLength(0)
				            sync = null
				            update.setLength(0)
			        	}
				    }
	        	}
	        	
	        	if (emptySS.get(subSequence.i-1) == 1) { // If the SubSequence is not empty
	        		// Add the location for the SubSequence end
	        		x = subSequence.actionC.size() * 2 * xOffset + xOffset
	        		y = subSequence.i * yOffset
					locName = String.format("s%d_end", subSequence.i)
					invariant = null
					rate_of_exp = null
					urgent = false
					committed = false
					builder.append(genLoc(id, x, y, locName, invariant, rate_of_exp, urgent, committed))
					// Add the transition from the last action complete location to the subsequence end
					source = lastActionID.get(subSequence.i)
					target = id
					select = null
					sync = null
					controllable = true
					nailX = 0
					nailY = 0
					transition.append(genTran(id+1, source, target, select, null, sync, String.format("%s_stat = true", sequence.agent.name.toLowerCase()), controllable, x-xOffset, y, nailX, nailY))
					endSubSeqID.set(subSequence.i, id)
					id = id + 2
	        	}
	        	
	        }
	        
	        for (i : 1..maxIndex) {
				if (emptySS.get(i - 1) == 0) {
					// Add the idle location for an empty subsequence
					x = xOffset
					y = i * yOffset
					locName = String.format("s%d_idle", i)
					invariant = null
					rate_of_exp = null
					urgent = false
					committed = false
					builder.append(genLoc(id, x, y, locName, invariant, rate_of_exp, urgent, committed))
					endSubSeqID.set(i, id)
					id++
				}
				emptySS.set(i - 1, 0)
			}
			
			for (subSequence : sequence.subSequence) {
				if (subSequence.j - subSequence.i > 0) {
					// In a continuation, the end location id is shared across the chained subsequences
					if (subSequence.j != 0) {
						for (i : subSequence.i + 1 .. subSequence.j) {
							endSubSeqID.set(i, endSubSeqID.get(i - 1))
						}
					}
				}
			}
			
			builder.append('''		<init ref="id0"/>
			''')
	        
	        // ==========================================================
	        // ==========================================================
	        
	        // ********* SEQUENCE ORCHESTRATOR TRANSITION ***************
	        
	        // ==========================================================
	        // ==========================================================

	   		var int firstAction = 0
			actionCount = 0
	   		
	        for (subSequence : sequence.subSequence) {
				// In case of continuation we do not want idle locations
				// except for an empty continuation
				if (subSequence.j != 0) {
					for (i : subSequence.i + 1 .. subSequence.j) {
						emptySS.set(i - 1, 1)
					}
				}
	            
	            firstAction = 1
	        	
	        	for (actionC : subSequence.actionC) {
					emptySS.set(subSequence.i - 1, 1) // The subsequence is not empty
	        		
					// Transition that brings to a busy action
					if (firstAction == 1) {  	// Is it the first busy action of the subSequence
						firstAction = 0 		// The next action will not be the first of the subSequence
						
						val agentPrefix = if (isHuman) "human" else "rob"
						val agentIndex = if (isHuman) "H-1" else "R-1"
						val config = getSeqActionConfig(actionC.actionType.toString(), agentPrefix)
						
						if (config !== null) {
							transition.append(genTran(id, subSequence.i, firstActionID.get(subSequence.i), null, null, config.startSync, if (config.hasBusyLoop) "t = 0" else null, true, 0, subSequence.i * yOffset, 0, 0))
							id++
						
							if (config.hasInitialUpdate) {
								update.append(getActionUpdate(model, actionC, agentPrefix, agentIndex))
							}
							transition.append(genTran(id, endSubSeqID.get(subSequence.i - 1), subSequence.i, null, null, String.format("s%d?", subSequence.i), update.toString(), true, 0, subSequence.i * yOffset - 100, 0, 0))
							id++
							update.setLength(0)
						
							if (config.hasBusyLoop) {
								transition.append(genTran(id, firstActionID.get(subSequence.i), subSequence.i, null, "t&gt;=5", null, null, true, xOffset * actionCount * 2, subSequence.i * yOffset + 100, xOffset * actionCount * 2 + 100, subSequence.i * yOffset + 80))
								id++
							}
						
							val actionGuard = getActionGuard(model, actionC, agentPrefix, agentIndex)
							if (!actionGuard.isEmpty) {
								guard.append(actionGuard)
							}
						
							val nextAction = getNextAction(subSequence.actionC, actionC)
							if (nextAction !== null) {
								val nextUpdate = getActionUpdate(model, nextAction, agentPrefix, agentIndex)
								if (!nextUpdate.isEmpty) {
									update.append(nextUpdate)
								}
							}
						
							transition.append(genTran(id, firstActionID.get(subSequence.i), firstActionID.get(subSequence.i) + 1, null, guard.toString(), config.stopSync, update.toString(), true, xOffset, subSequence.i * yOffset - 100, 0, 0))
							id++
							guard.setLength(0)
							update.setLength(0)
						}
						
					} else { // Not the first busy action of a subsequence

						actionCount++
					
						val agentPrefix = if (isHuman) "human" else "rob"
						val agentIndex = if (isHuman) "H-1" else "R-1"
						val config = getSeqActionConfig(actionC.actionType.toString(), agentPrefix)
					
						if (config !== null) {
					
							// From last complete to the busy location
							transition.append(genTran(
								id,
								firstActionID.get(subSequence.i) + actionCount * 2 - 1,
								firstActionID.get(subSequence.i) + actionCount * 2,
								null,
								null,
								config.startSync,
								if (config.hasBusyLoop) "t = 0" else null,
								true,
								xOffset * actionCount * 2,
								subSequence.i * yOffset,
								0,
								0
							))
							id++
					
							// Busy loop for actions that require repeated execution
							if (config.hasBusyLoop) {
								transition.append(genTran(
									id,
									firstActionID.get(subSequence.i) + actionCount * 2,
									firstActionID.get(subSequence.i) + actionCount * 2 - 1,
									null,
									"t&gt;=5",
									null,
									null,
									true,
									xOffset * actionCount * 2,
									subSequence.i * yOffset + 100,
									xOffset * actionCount * 2 + 100,
									subSequence.i * yOffset + 80
								))
								id++
							}
					
							// Completion guard for the current action
							val actionGuard = getActionGuard(model, actionC, agentPrefix, agentIndex)
							if (!actionGuard.isEmpty) {
								guard.append(actionGuard)
							}
					
							// Update variables according to the next action
							val nextAction = getNextAction(subSequence.actionC, actionC)
							if (nextAction !== null) {
								val nextUpdate = getActionUpdate(model, nextAction, agentPrefix, agentIndex)
								if (!nextUpdate.isEmpty) {
									update.append(nextUpdate)
								}
							}
					
							// From busy to complete
							transition.append(genTran(
								id,
								firstActionID.get(subSequence.i) + actionCount * 2,
								firstActionID.get(subSequence.i) + actionCount * 2 + 1,
								null,
								guard.toString(),
								config.stopSync,
								update.toString(),
								true,
								xOffset * (actionCount * 2 + 1),
								subSequence.i * yOffset - 100,
								0,
								0
							))
							id++
							guard.setLength(0)
							update.setLength(0)
						}
					}
					
	        	}
	        	
	        	actionCount = 0
	        }
	        
	        // LOCATIONs AND TRANSITIONs FOR CONDITION
	        
	        
	        // Transitions for idle subsequences
			for (i : 1 .. maxIndex) {
				if (emptySS.get(i - 1) == 0) {
					x = xOffset
					y = i * yOffset
			
					// Reach the actual subsequence location
					transition.append(genTran(id, endSubSeqID.get(i - 1), i, null, null, String.format("s%d?", i), null, true, 0, i * yOffset - 100, 0, 0))
					id++
			
					// Reach the idle location
					transition.append(genTran(id, i, endSubSeqID.get(i), null, null, null, String.format("%s_stat = true", sequence.agent.name.toLowerCase()), true, 0, i * yOffset, 0, 0))
					id++
				}
				emptySS.set(i - 1, 0)
			}
			
			// Add the transition to the final sequence location
			transition.append(genTran(id, endSubSeqID.get(maxIndex), endID, null, null, "e?", null, true, 0, (maxIndex + 1) * yOffset - 100, 0, 0))
			id++
			
			builder.append(transition.toString)
			transition.setLength(0)
			builder.append("\t</template>\n")
	    }
		
		return builder.toString
	}
	
	// Function to find the target index (Location or Resource)
	def int getTargetIndex(Model model, String targetName) {
		val locations = model.pattern.location.map[name.toString()].toList
		val locIndex = locations.indexOf(targetName)
		if (locIndex != -1) return locIndex
		
		val resources = model.pattern.resources.map[name.toString()].toList
		val resIndex = resources.indexOf(targetName)
		if (resIndex != -1) return locations.size + resIndex
		
		return -1
	}
	
	// Return true if the agent type supports the given action
	def boolean supportsAction(String agentPrefix, String actionType) {
		if (agentPrefix == "human") {
			return actionType == "moveTo"
		}
		
		if (agentPrefix == "rob") {
			return actionType == "moveTo" ||
				actionType == "follow" ||
				actionType == "pick" ||
				actionType == "deposit"
		}
		
		return false
	}
	
	// Function to generate the update assignments for a specific action
	def String getActionUpdate(Model model, ActionC action, String agentPrefix, String agentUppaalIndex) {
		val actionType = action.actionType.toString()
		
		// Return empty update if the action is not supported by the agent type
		if (!supportsAction(agentPrefix, actionType)) {
			return ""
		}
			
		switch (actionType) {
			case "moveTo": {
				val targetIndex = getTargetIndex(model, action.target.name.toString())
				val targetVar = if (agentPrefix == "human") "human_loc_target" else "robot_loc_target"
				return '''«targetVar»[«agentUppaalIndex»] = «targetIndex»'''.toString
			}
			case "pick": {
				val resIndex = model.pattern.resources.map[name.toString()].toList.indexOf(action.resource.name.toString())
				return '''robot_res_to_pick[«agentUppaalIndex»] = «resIndex», old_inventory = robot_inventory'''.toString
			}
			case "deposit": {
				val resIndex = model.pattern.resources.map[name.toString()].toList.indexOf(action.resource.name.toString())
				return '''robot_res_to_deposit[«agentUppaalIndex»] = «resIndex», old_inventory = robot_inventory'''.toString
			}
			case "follow": {
				val locIndex = model.pattern.location.map[name.toString()].toList.indexOf(action.target.name.toString())
				// Determine who is being followed
				val targetAgentData = resolveAgentPositionData(model, action.agent.name.toString()) 
				
				val humanFollowVar = if (targetAgentData?.key == "human") targetAgentData.value + 1 else 0
				val robotFollowVar = if (targetAgentData?.key == "rob") targetAgentData.value + 1 else 0
				
				return '''f_loc_target[«agentUppaalIndex»] = «locIndex»,
human_to_be_followed[«agentUppaalIndex»] = «humanFollowVar»,
robot_to_be_followed[«agentUppaalIndex»] = «robotFollowVar»'''.toString
			}
			default: return ""
		}
	}
	
	// Function to generate the completion guard for an action
	def String getActionGuard(Model model, ActionC action, String agentPrefix, String agentUppaalIndex) {
		val actionType = action.actionType.toString()
		val tolerance = 30
		
		// Return empty update if the action is not supported by the agent type
		if (!supportsAction(agentPrefix, actionType)) {
			return ""
		}
		
		switch (actionType) {
			case "moveTo": {
				val targetVar = if (agentPrefix == "human") "human_loc_target" else "robot_loc_target"
				return '''«agentPrefix»PositionX[«agentUppaalIndex»] &lt;= location_X[«targetVar»[«agentUppaalIndex»]]+«tolerance» and
	«agentPrefix»PositionX[«agentUppaalIndex»] &gt;= location_X[«targetVar»[«agentUppaalIndex»]]-«tolerance» and
	«agentPrefix»PositionY[«agentUppaalIndex»] &lt;= location_Y[«targetVar»[«agentUppaalIndex»]]+«tolerance» and
	«agentPrefix»PositionY[«agentUppaalIndex»] &gt;= location_Y[«targetVar»[«agentUppaalIndex»]]-«tolerance»'''.toString
			}
			
			case "pick": {
				return '''robot_inventory[«agentUppaalIndex»][robot_res_to_pick[«agentUppaalIndex»]] > old_inventory[«agentUppaalIndex»][robot_res_to_pick[«agentUppaalIndex»]]'''.toString
			}
			
			case "deposit": {
				return '''robot_inventory[«agentUppaalIndex»][robot_res_to_deposit[«agentUppaalIndex»]] &lt; old_inventory[«agentUppaalIndex»][robot_res_to_deposit[«agentUppaalIndex»]]'''.toString
			}
			
			case "follow": {
				val targetAgentData = resolveAgentPositionData(model, action.agent.name.toString())
				if (targetAgentData === null) return "// Error: Target agent not found"
				
				val tPrefix = targetAgentData.key
				val tVar = if (tPrefix == "human") "human_to_be_followed" else "robot_to_be_followed"
				
				// Guard: target agent must be at location and this agent must be at location
				return '''«tPrefix»PositionX[«tVar»[«agentUppaalIndex»]-1] &lt;= location_X[f_loc_target[«agentUppaalIndex»]]+«tolerance» and
	«tPrefix»PositionX[«tVar»[«agentUppaalIndex»]-1] &gt;= location_X[f_loc_target[«agentUppaalIndex»]]-«tolerance» and
	«tPrefix»PositionY[«tVar»[«agentUppaalIndex»]-1] &lt;= location_Y[f_loc_target[«agentUppaalIndex»]]+«tolerance» and
	«tPrefix»PositionY[«tVar»[«agentUppaalIndex»]-1] &gt;= location_Y[f_loc_target[«agentUppaalIndex»]]-«tolerance» and
	«agentPrefix»PositionX[«agentUppaalIndex»] &gt;= location_X[f_loc_target[«agentUppaalIndex»]]-«tolerance» and
	«agentPrefix»PositionX[«agentUppaalIndex»] &lt;= location_X[f_loc_target[«agentUppaalIndex»]]+«tolerance» and
	«agentPrefix»PositionY[«agentUppaalIndex»] &gt;= location_Y[f_loc_target[«agentUppaalIndex»]]-«tolerance» and
	«agentPrefix»PositionY[«agentUppaalIndex»] &lt;= location_Y[f_loc_target[«agentUppaalIndex»]]+«tolerance»'''.toString
			}
			
			default: return ""
		}
	}
			
	// Helper function to centralize agent resolution.
	// Returns a Pair with the array prefix (e.g., "human", "rob") and the agent index.
	def Pair<String, Integer> resolveAgentPositionData(Model model, String agentName) {
		
		val hIndex = model.pattern.human.map[name].toList.indexOf(agentName)
		if (hIndex != -1) return "human" -> hIndex
		
		val rIndex = model.pattern.robot.map[name].toList.indexOf(agentName)
		if (rIndex != -1) return "rob" -> rIndex
		
		// If you add a Drone later, you just add two lines here:
		// val dIndex = model.pattern.drone.map[name].toList.indexOf(agentName)
		// if (dIndex != -1) return "drone" -> dIndex
		
		return null
	}

	// Guards when the atomic predicate is position(agent, location or resource)
	def String apPositionGuard(Model model, String agentName, String targetName) {
		
		// Define the tolerance for the bounding box (distance from the center)
		val int tolerance = 30
		
		// 1. Find the target index using the unified helper method (Location or Resource)
		val targetIndex = getTargetIndex(model, targetName)
		
		// 2. Dynamically resolve the agent's array prefix and index
		val agentData = resolveAgentPositionData(model, agentName)
		
		if (agentData !== null && targetIndex != -1) {
			val prefix = agentData.key
			val index = agentData.value
			
			// 3. Return the generic template using the tolerance variable and the resolved targetIndex
			return '''«prefix»PositionX[«index»] &lt;= location_X[«targetIndex»]+«tolerance» and
«prefix»PositionX[«index»] &gt;= location_X[«targetIndex»]-«tolerance» and
«prefix»PositionY[«index»] &lt;= location_Y[«targetIndex»]+«tolerance» and
«prefix»PositionY[«index»] &gt;= location_Y[«targetIndex»]-«tolerance»'''.toString
		}
		
		return "// Error: Agent or Target not found"
	}


	// ==================================================
	// FUNCTION FOR LOCATION - TRANSITION - VERIFIER
	// ==================================================
	
	// Function that generates a location in Uppaal
	def String genLoc(int id, int x, int y, String name, String invariant, String rate_of_exp, boolean urgent, boolean committed) {
		return '''
			<location id="id«id»" x="«x»" y="«y»">
				«IF name !== null»
				<name x="«x - 17»" y="«y - 34»">«name»</name>
				«ENDIF»
				«IF invariant !== null»
				<label kind="invariant" x="«x - 10»" y="«y + 17»">«invariant»</label>
				«ENDIF»
				«IF rate_of_exp !== null»
				<label kind="exponentialrate" x="«x - 10»" y="«y + 34»">«rate_of_exp»</label>
				«ENDIF»
				«IF urgent && !committed»
				<urgent/>
				«ENDIF»
				«IF committed && !urgent»
				<committed/>
				«ENDIF»
			</location>
		'''.toString
	}
	
	// Function that generates a transition in Uppaal
	def String genTran(int id, int source, int target, String select, String guard, String sync, String update, boolean controllable, int xOffset, int yOffset, int nailX, int nailY) {
		
		// 1. Pre-calculate the base coordinates to avoid code duplication
		val isPatternOrch = (xOffset == 500 && yOffset == 0)
		val baseX = if (isPatternOrch) xOffset * source else xOffset
		val baseY = if (isPatternOrch) yOffset * source else yOffset
		
		// 2. Use Xtend template expressions for clean XML generation
		return '''
			<transition id="id«id»"«IF !controllable» controllable="false"«ENDIF»>
				<source ref="id«source»"/>
				<target ref="id«target»"/>
				«IF select !== null»
				<label kind="select" x="«17 + baseX»" y="«-51 + baseY»">«select»</label>
				«ENDIF»
				«IF guard !== null»
				<label kind="guard" x="«18 + baseX»" y="«-34 + baseY»">«guard»</label>
				«ENDIF»
				«IF sync !== null»
				<label kind="synchronisation" x="«18 + baseX»" y="«-17 + baseY»">«sync»</label>
				«ENDIF»
				«IF update !== null»
				<label kind="assignment" x="«18 + baseX»" y="«0 + baseY»">«update»</label>
				«ENDIF»
				«IF nailX !== 0 && nailY !== 0»
				<nail x="«nailX»" y="«nailY»"/>
				«ENDIF»
			</transition>
		'''.toString
	}
	
	// Function that generates verifier formulas
	def String verifier(Model model) {
		val simTime = 700
		val pattern = model.pattern
		val patternName = pattern.name.toString

		// 1. maxIndex calculation
		val maxIndex = model.sequence
			.flatMap[subSequence]
			.map[Math.max(i, j)]
			.max ?: 0

		val queries = new java.util.ArrayList<String>()

		/* --- Single Sequence Queries --- */ // = The whole pattern finish and each sub-sequence init - idle - end is reached
		for (seq : model.sequence) {
			val continuation = newIntArrayOfSize(maxIndex)
			val emptySS = newIntArrayOfSize(maxIndex)
			val agent = seq.agent.name.toLowerCase

			for (ss : seq.subSequence) {
				if (ss.j > ss.i) {
					for (k : ss.i + 1 .. ss.j) {
						continuation.set(k - 1, 1)
						emptySS.set(k - 1, 1)
					}
				}
				if (ss.actionC !== null && !ss.actionC.empty) {
					emptySS.set(ss.i - 1, 1)
				}
			}

			val states = (1 .. maxIndex).map[ i |
				val c = continuation.get(i - 1)
				val e = emptySS.get(i - 1)
				
				// If it is not a continuation, we always have a start state
				if (c == 0) {
					val startState = '''«patternName»_«agent»_def.s«i»_start'''
					val endOrIdleState = if (e == 0) '''«patternName»_«agent»_def.s«i»_idle''' else '''«patternName»_«agent»_def.s«i»_end'''
					
					// Return both states separated by a comma
					'''«startState», «endOrIdleState»'''
				} else {
					null
				}
			].filterNull.join(", ")

			queries.add('''
				<query>
					<formula>simulate[&lt;=«simTime»]{«states», «patternName».end}</formula>
				</query>
			''')
		}

		/* --- General Pattern Queries --- */
		val hCount = pattern.human.size
		val rCount = pattern.robot.size
		val resCount = pattern.resources.size

		// Pattern termination
		queries.add('''
			<query>
				<formula>Pr[&lt;=«simTime»] (&lt;&gt; «patternName».end)</formula>
				<comment>Check if the whole pattern ends within «simTime» time unit</comment>
			</query>
		''')

		// Agent Positions
		val posParts = new java.util.ArrayList<String>()
		if (hCount > 0) posParts.addAll((0 .. hCount - 1).map['''humanPositionX[«it»], humanPositionY[«it»]'''])
		if (rCount > 0) posParts.addAll((0 .. rCount - 1).map['''robPositionX[«it»], robPositionY[«it»], batteryCharge[«it»]'''])
		
		if (!posParts.empty) {
			queries.add('''
				<query>
					<formula>simulate[&lt;=«simTime»]{«posParts.join(", ")», «patternName».end}</formula>
					<comment>Check the position of all the agent in the pattern for «simTime» time unit</comment>
				</query>
			''')
		}

		// Robot Inventories
		if (rCount > 0 && resCount > 0) {
			val invs = (0 .. rCount - 1).flatMap[ r | 
				(0 .. resCount - 1).map[ res | '''robot_inventory[«r»][«res»]'''] 
			].join(", ")

			queries.add('''
				<query>
					<formula>simulate[&lt;=«simTime»]{«invs», «patternName».end*2}</formula>
					<comment>Check quantity of each item for each robot</comment>
				</query>
			''')
		}

		// Location Positions
		if (!pattern.location.nullOrEmpty) {
			val locs = pattern.location.map[name + "[0], " + name + "[1]"].join(", ")
			queries.add('''
				<query>
					<formula>simulate[&lt;=«simTime»]{«locs»}</formula>
					<comment>Plot the position of each location in the pattern</comment>
				</query>
			''')
		}

		// Robot Modes
		if (rCount > 0) {
			val rmodes = (1 .. rCount).map[ i | '''rm_«i».idle, rf_«i».idle, rp_«i».idle, rd_«i».idle''' ].join(", ")
			queries.add('''
				<query>
					<formula>simulate[&lt;=«simTime»]{«rmodes»}</formula>
					<comment></comment>
				</query>
			''')
			
			val rmodes2 = (1 .. rCount).map[ i | '''!rm_«i».idle || !rf_«i».idle || !rp_«i».idle || !rd_«i».idle''' ].join(" || ")
			queries.add('''
				<query>
					<formula>Pr[&lt;=«simTime»](&lt;&gt;«rmodes2»)</formula>
					<comment></comment>
				</query>
			''')
			
			val rmodes3 = (1 .. rCount).map[ i | '''rm_«i».idle &amp;&amp; rf_«i».idle &amp;&amp; rp_«i».idle &amp;&amp; rd_«i».idle''' ].join(" &amp;&amp; ")
			queries.add('''
				<query>
					<formula>Pr[&lt;=«simTime»]([] («patternName».end imply («rmodes3»)))</formula>
					<comment></comment>
				</query>
			''')
		}
		
		// Reachability of Targets for Robots and Humans (MoveTo check)
		val robotList = pattern.robot.map[name]
		val humanList = pattern.human.map[name]
		
		for (seq : model.sequence) {
			val rIndex = robotList.indexOf(seq.agent.name)
			val hIndex = humanList.indexOf(seq.agent.name)
			
			val isRobot = rIndex != -1
			val isHuman = hIndex != -1
			
			if (isRobot || isHuman) {
				val targets = new java.util.HashSet<String>()
				for (ss : seq.subSequence) {
					for (act : ss.actionC) {
						if (act.actionType == "moveTo") {
							targets.add(act.target.name)
						}
					}
				}
				
				val posX = if (isRobot) "robPositionX" else "humanPositionX"
				val posY = if (isRobot) "robPositionY" else "humanPositionY"
				val idx = if (isRobot) rIndex else hIndex
				val typeName = if (isRobot) "robot" else "human"

				for (tName : targets) {
					queries.add('''
						<query>
							<formula>Pr[&lt;=«simTime»] (&lt;&gt; pow(«posX»[«idx»] - «tName»[0], 2) + pow(«posY»[«idx»] - «tName»[1], 2) &lt;= 900 )</formula>
							<comment>Check if «typeName» «seq.agent.name» reaches «tName» (within 30 units)</comment>
						</query>
					''')
				}
			}
		}
		
		// Valid Position / Collision Check
		pattern.human.forEach[ h, i |
			queries.add('''
				<query>
					<formula>Pr[&lt;=«simTime»] (&lt;&gt; !isValidPosition(humanPositionX[«i»], humanPositionY[«i»]))</formula>
					<comment>Check if human «h.name» enters an invalid position (e.g. collision or obstacle)</comment>
				</query>
			''')
		]
		
		pattern.robot.forEach[ r, i |
			queries.add('''
				<query>
					<formula>Pr[&lt;=«simTime»] (&lt;&gt; !isValidPosition(robPositionX[«i»], robPositionY[«i»]))</formula>
					<comment>Check if robot «r.name» enters an invalid position (e.g. collision or obstacle)</comment>
				</query>
			''')
		]
		
		queries.add('''
				<query>
					<formula>Pr[&lt;=«simTime»] (&lt;&gt; currP > 2)</formula>
					<comment>Check if pattern is executed more then once</comment>
				</query>
			''')
		
		return queries.join("\n")
	}


}

