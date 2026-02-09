package it.polimi.generator

import it.polimi.lIRAs.Model
import java.io.FileInputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import javax.swing.text.rtf.RTFEditorKit
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext

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
            
            // Define the initial position of each Human (as default all the humans start in the same position)
            replace(filePath, "// **human_start_pos**", hInitPos (model), fsa)
            
            // Generate the declarations to initialize a Robot agent (SoC, position)
            replace(filePath, "// **robot_initialization**", rInit (model), fsa)
            
            // Define the initial position of each Robot (as default all the humans start in the same position)
      		replace(filePath, "// **robot_start_pos**", rInitPos (model), fsa)
      		
        	// HUMAN ACTIONS
        	hmoveTo(filePath, fsa, model)
        	hidle(filePath, fsa, model)
        	        	
        	// ROBOT ACTION
        	rmoveTo(filePath, fsa, model)
        	rfollow(filePath, fsa, model)
			val int iNum = model.pattern.resources.size
			if (iNum !== 0) {
	        	rpick(filePath, fsa, model)
	        	rdeposit(filePath, fsa, model)
        	}
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

		// To use relatives path use something like this. Be sure that the folder with the template exist in this position
			//val inputStream = getClass().getResourceAsStream("/it/polimi/generator/template/Pre_Gen.xml")
	        
	        //if (inputStream === null) {
	        //    return "Error: Template not found (Check build.properties includes)"
	        //}
	        //val scanner = new java.util.Scanner(inputStream, "UTF-8")
	        //val content = if (scanner.hasNext()) scanner.useDelimiter("\\A").next() else ""
	        
	        //scanner.close()
	        //inputStream.close()
	        
	        //return content
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
    def ssChan (Model model) {
        // Declarations
        val int[] maxSSnum = #[0]
        val StringBuilder builder = new StringBuilder
        
        // Check for the higher subSequence number
        model.sequence.forEach[sequence |
            sequence.subSequence.forEach[subSequence |
                if (subSequence.i > maxSSnum.get(0)) {
                    maxSSnum.set(0, subSequence.i)
                }
                if (subSequence.j > maxSSnum.get(0)) {
                    maxSSnum.set(0, subSequence.j)
                }
            ]
        ]
        
        // Generate the string for the declaration
        builder.append("urgent broadcast chan ")
        // Define one channel for each SubSequence
        for (i : 1..maxSSnum.get(0)) {
            if (i > 1) {
                builder.append(", ")
            }
            builder.append(String.format("s%d", i))
        }
        // Final channel that is present for each pattern
        builder.append(", e;")
        return builder.toString
    }
    
    // Function that declare the initial status of each Sequence as true
    def sStatus (Model model) {
    	// Declaration
    	val StringBuilder builder = new StringBuilder
    	
    	// Declaration
    	model.sequence.forEach[sequence |
            builder.append(String.format("bool %s_stat = true;\n", sequence.agent.name.toLowerCase()))
    	]
    	return builder.toString
	}

	// Function that generate the locations array for the input location AND resources
	def String iLocation(Model model) {
	    // 1. Get lists with null-safety (elvis operator)
	    val locations = model.pattern.location ?: emptyList
	    val resources = model.pattern.resources ?: emptyList
	    
	    // 2. Calculate total size
	    val totalSize = locations.size + resources.size
		
	    // 3. Safety check
	    if (totalSize == 0) {
	        return '''
// No locations or resources declared. Using dummy to prevent compilation errors.
	        '''.toString
	    }
	
	    // 4. Generate the strings for X
	    val locX = locations.map[name + "[0]"]
	    val resX = resources.map[name + "[0]"]
	    val stringX = (locX + resX).join(", ")
	
	    // 5. Generate the strings for Y
	    val locY = locations.map[name + "[1]"]
	    val resY = resources.map[name + "[1]"] // Fixed: was [0] in your snippet
	    val stringY = (locY + resY).join(", ")
		
	    return '''
double location_X[«totalSize»] = {«stringX»};
double location_Y[«totalSize»] = {«stringY»};
	    '''.toString
	}

	// Function that generate the array for the input resource
	def String iResource(Model model) {
	    val resources = model.pattern.resources
	
	    // Safety check: if list is null or empty
	    if (resources.nullOrEmpty) {
	        return '''
// No resources in pattern
	        '''.toString
	    }
	
	    val size = resources.size
	    
	    return '''
double resources_X[«size»] = {«resources.map[name + "[0]"].join(", ")»};
double resources_Y[«size»] = {«resources.map[name + "[1]"].join(", ")»};
	    '''.toString
	}

	// Function that declare the number of humans in the pattern, if any
	// (n.b. an agent that is not used for a SubSequence can still be needed for the conditions if passed as input, so it has to be fully defined
	def hNumber (Model model) {
		// Declarations
    	val StringBuilder builder = new StringBuilder
    	val int hNum = model.pattern.human.size
    	
    	// If there are humans in the pattern, declare how much they are
    	if (hNum !== 0) {
	    	builder.append(String.format("const int N_H = %d; // Number of humans in the pattern", hNum))
    	}else{
			builder.append("// There are no Humans in the pattern Input")
		}
    	
    	return builder.toString
	}
	
	// Function that declare the number of robots in the pattern, if any
	def rNumber (Model model) {
    	val StringBuilder builder = new StringBuilder
    	val int rNum = model.pattern.robot.size
    	
    	// If there are robots in the pattern, declare how much they are
		if (rNum !== 0) {
	    	builder.append(String.format("const int N_R = %d; // Number of robots in the pattern", rNum))
    	}else{
			builder.append("// There are no Robots in the pattern Input")
		}
    	
    	return builder.toString
	}
	
	// Function that declare the number of resources in the pattern, if any
	def iNumber (Model model) {
    	val StringBuilder builder = new StringBuilder
    	val int iNum = model.pattern.resources.size
    	
    	// If there are robots in the pattern, declare how much they are
		if (iNum !== 0) {
	    	builder.append(String.format("const int N_ITEMS = %d; // Number of resources in the pattern", iNum))
    	}else{
			builder.append("// There are no Resources in the pattern Input")
		}
    	
    	return builder.toString
	}
	
	// Function that initialize the variables necessary for a Human agent
	def hInit (Model model) {
		// Declarations
		val StringBuilder builder = new StringBuilder
    	val int hNum = model.pattern.human.size
		
		
		if (hNum !== 0) {
		builder.append("// Human initialization\n")
			// Declare the Human fatigue
			builder.append("double humanFatigue[N_H] = {")
			for (i : 1..hNum) {
				builder.append("0.0")
				if (i!=hNum) {
					builder.append(", ")
				}
			}
			builder.append("};\n")
			
			// Human X
			builder.append("double humanPositionX[N_H] = {")
			for (i : 1..hNum) {
				builder.append("0.0")
				if (i!=hNum) {
					builder.append(", ")
				}
			}
			builder.append("};\n")
			
			// Human Y
			builder.append("double humanPositionY[N_H] = {")
			for (i : 1..hNum) {
				builder.append("0.0")
				if (i!=hNum) {
					builder.append(", ")
				}
			}
			builder.append("};")
		}
		if (hNum == 0){
			builder.append("double humanPositionX[1] = {0.0};\ndouble humanPositionY[1] = {0.0};")
		}
		return builder.toString
	}
	
	// Function that define the starting position of each Human
	def hInitPos (Model model) {
		// Declarations
		val StringBuilder builder = new StringBuilder
    	val int hNum = model.pattern.human.size
		
		// Starting x
		if (hNum !== 0) {
			builder.append("double h_startX[N_H] = {")
			for (i : 1..hNum) {
				builder.append("2500.0")
				if (i!=hNum) {
					builder.append(", ")
				}
			}
			builder.append("};\n")
			
			// Starting y
			builder.append("double h_startY[N_H] = {")
			for (i : 1..hNum) {
				builder.append("100.0")
				if (i!=hNum) {
					builder.append(", ")
				}
			}
			builder.append("};")
		}
		
		return builder.toString
	}
	
	// Function that initialize the variables necessary for a Robot agent
	def rInit (Model model) {
		val StringBuilder builder = new StringBuilder
    	val int rNum = model.pattern.robot.size
		if (rNum !== 0){
			builder.append("// Robot initialization\n")
			
			// Battery charge
			builder.append("double batteryCharge[N_R] = {")
			for (i : 1..rNum) {
				builder.append("100.0")
				if (i != rNum) {
					builder.append(", ")
				}
			}
			builder.append("};\n")
			
			// Robot position X
			builder.append("double robPositionX[N_R] = {")
			for (i : 1..rNum) {
				builder.append("0.0")
				if (i != rNum) {
					builder.append(", ")
				}
			}
			builder.append("};\n")
			
			// Robot position Y
			builder.append("double robPositionY[N_R] = {")
			for (i : 1..rNum) {
				builder.append("0.0")
				if (i != rNum) {
					builder.append(", ")
				}
			}
			builder.append("};\n")
			
		}else {
			builder.append("// None input Robot has been declared")
		}
		
		return builder.toString
	}
	
	// Function that define the starting position of each Robot
	def rInitPos (Model model) {
		val StringBuilder builder = new StringBuilder
    	val int rNum = model.pattern.robot.size
		
		if (rNum !== 0) {
			// Robot Starting x position
			builder.append("double r_startX[N_R] = {")
			for (i : 1..rNum) {
				builder.append("2000.0")
				if (i!=rNum) {
					builder.append(", ")
				}
			}
			builder.append("};\n")
			// Robot Starting y position
			builder.append("double r_startY[N_R] = {")
			for (i : 1..rNum) {
				builder.append("500.0")
				if (i!=rNum) {
					builder.append(", ")
				}
			}
			builder.append("};")
		}
		
		return builder.toString
	}
	
	// Function that include the necessary declaration for a Human moveTo action and add the call the function that add the moveTo template
	def hidle (String filePath,IFileSystemAccess2 fsa, Model model) {
		// Declaration
		val int hNum = model.pattern.human.size
		
		if (hNum !== 0) {
			
			// Define the channel for start and stop the start and stop the moveTo Human action
			replace(filePath, "// **h_idle_sync**", "urgent broadcast chan obey_h_moveTo[N_H];", fsa)
	        
	        // Add the Human moveTo template
	        replace(filePath, "// **Human_idle**", readHidle(), fsa)
	        
	        // Define the Human moveTo
	        replace(filePath, "// **missDef_hidle**", missDef_hidle(model), fsa)
	        
	        // Declare the Human moveTo
	        replace(filePath, "// **missDec_hidle**", missDec_hidle(model), fsa)
		}
		
	}
	def String readHidle () {
    	val filePath = "/Users/alberto_tagliaferro/Library/CloudStorage/OneDrive-PolitecnicodiMilano/Desktop/LIRAs_Generation/runtime-EclipseApplication/src-gen/Human_idle.rtf"
    	
    	try {
     	   val fis = new FileInputStream(filePath)
        	val rtfKit = new RTFEditorKit()
        	val doc = rtfKit.createDefaultDocument()
        	rtfKit.read(fis, doc, 0)
        	
        	// Estract the text from the document
        	val text = doc.getText(0, doc.getLength())
        	fis.close()
        
        	return text
        	
    	} catch (Exception e) {
        	e.printStackTrace
        	return "Error reading file"
    	}
	}
	// Human moveTo mission definition
	def missDef_hidle (Model model) {
		// Declaration
		val StringBuilder builder = new StringBuilder
    	val int hNum = model.pattern.human.size  
    	
    	if (hNum !== 0) {    		
			for (i : 1..hNum) {
				builder.append(String.format("hi_%d = Human_idle(%d);", i, i))
				if (i!=hNum) {
					builder.append("\n")
				}
			}
    	}
    			
		return builder.toString
	}
	// Human moveTo mission Declaration
	def missDec_hidle (Model model) {
		// Declarations
		val StringBuilder builder = new StringBuilder
    	val int hNum = model.pattern.human.size  
    	
    	if (hNum !== 0) {
   			for (i : 1..hNum) {
				builder.append(String.format("hi_%d,", i))
				if (i!=hNum) {
					builder.append("\n")
				}
			}
    	}

		return builder.toString
	}
	
	// Function that include the necessary declaration for a Human moveTo action and add the call the function that add the moveTo template
	def hmoveTo (String filePath,IFileSystemAccess2 fsa, Model model) {
		// Declaration
		val int hNum = model.pattern.human.size
		
		if (hNum !== 0) {
			
			// Define the channel for start and stop the start and stop the moveTo Human action
			replace(filePath, "// **h_moveTo_sync**", "urgent broadcast chan start_h_moveTo[N_H], stop_h_moveTo[N_H];", fsa)
	        
	        // Initialize the variable used to select the target location of each moveTo action
	        replace(filePath, "// **h_moveTo_var**", "int human_loc_target[N_H];", fsa)
	        
	        // Define the moveTo fail synchronization channel
	        replace(filePath, "// **h_moveTo_chan**", "broadcast chan hm_fail;", fsa)
	        
	        val int lNum = model.pattern.location.size
	        val int resNum = model.pattern.resources.size
        	if( lNum !== 0 || resNum !== 0){
		        // Add the Human moveTo template
		        replace(filePath, "// **Human_moveTo**", readHmoveTo(), fsa)
		        
		        // Define the Human moveTo
		        replace(filePath, "// **missDef_hm**", missDef_hm(model), fsa)
		        
		        // Declare the Human moveTo
		        replace(filePath, "// **missDec_hm**", missDec_hm(model), fsa)
	        }
	    }
	}
		
	// Read and include the moveTo Human template
	def String readHmoveTo () {
    	val filePath = "/Users/alberto_tagliaferro/Library/CloudStorage/OneDrive-PolitecnicodiMilano/Desktop/LIRAs_Generation/runtime-EclipseApplication/src-gen/Human_moveTo.rtf"
    	try {
     	   val fis = new FileInputStream(filePath)
        	val rtfKit = new RTFEditorKit()
        	val doc = rtfKit.createDefaultDocument()
        	rtfKit.read(fis, doc, 0)
        	
        	// Estract the text from the document
        	val text = doc.getText(0, doc.getLength())
        	fis.close()
        
        	return text
        	
    	} catch (Exception e) {
        	e.printStackTrace
        	return "Error reading file"
    	}
	}
	// Human moveTo mission definition
	def missDef_hm (Model model) {
		// Declaration
		val StringBuilder builder = new StringBuilder
    	val int hNum = model.pattern.human.size  
    	
    	if (hNum !== 0) {    		
			for (i : 1..hNum) {
				builder.append(String.format("hm_%d = Human_moveTo(%d, 26.0);", i, i))
				if (i!=hNum) {
					builder.append("\n")
				}
			}
    	}
    			
		return builder.toString
	}
	// Human moveTo mission Declaration
	def missDec_hm (Model model) {
		// Declarations
		val StringBuilder builder = new StringBuilder
    	val int hNum = model.pattern.human.size  
    	
    	if (hNum !== 0) {
   			for (i : 1..hNum) {
				builder.append(String.format("hm_%d,", i))
				if (i!=hNum) {
					builder.append("\n")
				}
			}
    	}  			

		return builder.toString
	}
	
	// Function that include the necessary declaration for a Robot moveTo action and add the call the function that add the moveTo template	
	def rmoveTo (String filePath,IFileSystemAccess2 fsa, Model model) {
		// Declaration
		val int rNum = model.pattern.robot.size
		
		if (rNum !== 0) {
			// Define the channel for start and stop the start and stop the moveTo Robot action
			replace(filePath, "// **r_moveTo_sync**", "urgent broadcast chan start_r_moveTo[N_R], stop_r_moveTo[N_R];", fsa)
	
	        // Initialize the variable used to select the target location of each moveTo action        
	        replace(filePath, "// **r_moveTo_var**", "int robot_loc_target[N_R];", fsa)
	        
	        // Define the moveTo fail synchronization channel
	        replace(filePath, "// **r_moveTo_chan**", "broadcast chan rm_still;", fsa)
	        
	        val int lNum = model.pattern.location.size
	        val int resNum = model.pattern.resources.size
        	if( lNum !== 0 || resNum !== 0){
        		// Add the Robot moveTo template
		        replace(filePath, "// **Robot_moveTo**", readRmoveTo(), fsa)
		        
		        // Define the Robot moveTo
		        replace(filePath, "// **missDef_rm**", missDef_rm(model), fsa)
		        
		        // Declare the Robot moveTo
		        replace(filePath, "// **missDec_rm**", missDec_rm(model), fsa)
        	}
	        
		}
		
	}
	// Read and include the moveTo Robot template
	def String readRmoveTo () {
    	val filePath = "/Users/alberto_tagliaferro/Library/CloudStorage/OneDrive-PolitecnicodiMilano/Desktop/LIRAs_Generation/runtime-EclipseApplication/src-gen/Robot_moveTo.rtf"
    	try {
     	   val fis = new FileInputStream(filePath)
        	val rtfKit = new RTFEditorKit()
        	val doc = rtfKit.createDefaultDocument()
        	rtfKit.read(fis, doc, 0)
        	val text = doc.getText(0, doc.getLength())
        	fis.close()
        	
        	return text
    	} catch (Exception e) {
        	e.printStackTrace
        	
        	return "Error reading file"
    	}
	}
	// Robot moveTo mission definition
	def missDef_rm (Model model) {
		// Declarations
		val StringBuilder builder = new StringBuilder
    	val int rNum = model.pattern.robot.size    			
		
		for (i : 1..rNum) {
			builder.append(String.format("rm_%d = Robot_moveTo(%d, 26.0, 26.0);", i, i))
			if (i!=rNum) {
				builder.append("\n")
			}
		}
		
		return builder.toString
	}
	// Robot moveTo mission declaration
	def missDec_rm (Model model) {
		// Declarations
		val StringBuilder builder = new StringBuilder
    	val int rNum = model.pattern.robot.size    			
		
		for (i : 1..rNum) {
			builder.append(String.format("rm_%d,", i))
			if (i!=rNum) {
				builder.append("\n")
			}
		}
		
		return builder.toString
	}
	
	// Functions that include the necessary declaration for a Robot follow action and add the call the function that add the moveTo template	
	def rfollow (String filePath,IFileSystemAccess2 fsa, Model model) {
		// Declaration
		val int rNum = model.pattern.robot.size
		if (rNum !== 0) {
			replace(filePath, "// **r_follow_sync**", "urgent broadcast chan start_r_follow[N_R], stop_r_follow[N_R];", fsa)
	        replace(filePath, "// **r_follow_var**", "// Who I have to follow (agent id)?\nint human_to_be_followed[N_R];\nint robot_to_be_followed[N_R];\nint f_loc_target[N_R];", fsa)
	        replace(filePath, "// **r_follow_chan**", "broadcast chan rf_still;", fsa)
	        replace(filePath, "// **Robot_follow**", readRfollow(), fsa)
	        replace(filePath, "// **missDef_rf**", missDef_rf(model), fsa)
	        replace(filePath, "// **missDec_rf**", missDec_rf(model), fsa)
        }
	}
	def String readRfollow () {
    	val filePath = "/Users/alberto_tagliaferro/Library/CloudStorage/OneDrive-PolitecnicodiMilano/Desktop/LIRAs_Generation/runtime-EclipseApplication/src-gen/Robot_follow.rtf"
    	try {
     	   val fis = new FileInputStream(filePath)
        	val rtfKit = new RTFEditorKit()
        	val doc = rtfKit.createDefaultDocument()
        	rtfKit.read(fis, doc, 0)
        	val text = doc.getText(0, doc.getLength())
        	fis.close()
        
        	return text
    	} catch (Exception e) {
        	e.printStackTrace
        	return "Error reading file"
    	}
	}
	def missDef_rf (Model model) {
		val StringBuilder builder = new StringBuilder
    	val int rNum = model.pattern.robot.size    			
		for (i : 1..rNum) {
			builder.append(String.format("rf_%d = Robot_follow(%d, 26.0, 26.0);", i, i))
			if (i!=rNum) {
				builder.append("\n")
			}
		}
		
		return builder.toString
	}
	def missDec_rf (Model model) {
		val StringBuilder builder = new StringBuilder
    	val int rNum = model.pattern.robot.size    			
		for (i : 1..rNum) {
			builder.append(String.format("rf_%d,", i))
			if (i!=rNum) {
				builder.append("\n")
			}
		}
		return builder.toString
	}
	
	// Functions that include the necessary declaration for a Robot pick action and add the call the function that add the pick template	
	def rpick (String filePath, IFileSystemAccess2 fsa, Model model) {
		val int rNum = model.pattern.robot.size
		val int iNum = model.pattern.resources.size

		if (rNum !== 0) {
			replace(filePath, "// **r_pick_sync**", "urgent broadcast chan start_r_pick[N_R], stop_r_pick[N_R];", fsa)
	        
			var String inventoryVars
			if (iNum !== 0) {
				inventoryVars = "int robot_res_to_pick[N_R];\nint robot_inventory[N_R][N_ITEMS];\nint old_inventory[N_R][N_ITEMS];"
			} else {
				inventoryVars = "// No resources defined, inventory variables skipped"
			}
			
			replace(filePath, "// **r_pick_var**", inventoryVars, fsa)
	        replace(filePath, "// **Robot_pick**", readRpick(), fsa)
	        replace(filePath, "// **missDef_rp**", missDef_rp(model), fsa)
	        replace(filePath, "// **missDec_rp**", missDec_rp(model), fsa)
        }
	}
	def String readRpick () {
    	val filePath = "/Users/alberto_tagliaferro/Library/CloudStorage/OneDrive-PolitecnicodiMilano/Desktop/LIRAs_Generation/runtime-EclipseApplication/src-gen/Robot_pick.rtf"
    	try {
     	   val fis = new FileInputStream(filePath)
        	val rtfKit = new RTFEditorKit()
        	val doc = rtfKit.createDefaultDocument()
        	rtfKit.read(fis, doc, 0)
        	val text = doc.getText(0, doc.getLength())
        	fis.close()
        
        	return text
    	} catch (Exception e) {
        	e.printStackTrace
        	return "Error reading Robot_pick.rtf file"
    	}
	}
	def missDef_rp (Model model) {
		val StringBuilder builder = new StringBuilder
    	val int rNum = model.pattern.robot.size    			
		for (i : 1..rNum) {
			builder.append(String.format("rp_%d = Robot_pick(%d);", i, i))
			if (i!=rNum) {
				builder.append("\n")
			}
		}
		return builder.toString
	}
	def missDec_rp (Model model) {
		val StringBuilder builder = new StringBuilder
    	val int rNum = model.pattern.robot.size    			
		for (i : 1..rNum) {
			builder.append(String.format("rp_%d,", i))
			if (i!=rNum) {
				builder.append("\n")
			}
		}
		return builder.toString
	}
	
	// Functions that include the necessary declaration for a Robot deposit action and add the call the function that add the deposit template	
	def rdeposit (String filePath,IFileSystemAccess2 fsa, Model model) {
		// Declere it only if there is some robot
		val int rNum = model.pattern.robot.size
		if (rNum !== 0) {
			replace(filePath, "// **r_deposit_sync**", "urgent broadcast chan start_r_deposit[N_R], stop_r_deposit[N_R];", fsa)
	        replace(filePath, "// **r_deposit_var**", "int robot_res_to_deposit[N_R];", fsa)
	        replace(filePath, "// **Robot_deposit**", readRdeposit(), fsa)
	        replace(filePath, "// **missDef_rd**", missDef_rd(model), fsa)
	        replace(filePath, "// **missDec_rd**", missDec_rd(model), fsa)
        }
	}
	def String readRdeposit () {
    	val filePath = "/Users/alberto_tagliaferro/Library/CloudStorage/OneDrive-PolitecnicodiMilano/Desktop/LIRAs_Generation/runtime-EclipseApplication/src-gen/Robot_deposit.rtf"
    	try {
     	   val fis = new FileInputStream(filePath)
        	val rtfKit = new RTFEditorKit()
        	val doc = rtfKit.createDefaultDocument()
        	rtfKit.read(fis, doc, 0)
        	val text = doc.getText(0, doc.getLength())
        	fis.close()
        
        	return text
    	} catch (Exception e) {
        	e.printStackTrace
        	return "Error reading Robot_deposit.rtf file"
    	}
	}
	def missDef_rd (Model model) {
		val StringBuilder builder = new StringBuilder
    	val int rNum = model.pattern.robot.size    			
		for (i : 1..rNum) {
			builder.append(String.format("rd_%d = Robot_deposit(%d);", i, i))
			if (i!=rNum) {
				builder.append("\n")
			}
		}
		return builder.toString
	}
	def missDec_rd (Model model) {
		val StringBuilder builder = new StringBuilder
    	val int rNum = model.pattern.robot.size    			
		for (i : 1..rNum) {
			builder.append(String.format("rd_%d,", i))
			if (i!=rNum) {
				builder.append("\n")
			}
		}
		return builder.toString
	}
	
	// Definition for the Orchestrators
	def pattDef (Model model) {
		val StringBuilder builder = new StringBuilder
    	val int[] rNum = #[1]  
    	val int[] hNum = #[1]
		
		builder.append(String.format("%s = %s_orchestrator(1);\n", model.pattern.name.toString(), model.pattern.name.toString()))
		
		model.sequence.forEach[ sequence |
			if (model.pattern.human.map[name].contains(sequence.agent.name)) {
				builder.append(String.format("%s_%s_def = %s_%s(1, %d);\n", model.pattern.name.toString(), sequence.agent.name.toLowerCase(), model.pattern.name.toString(), sequence.agent.name.toLowerCase(), hNum.get(0)))
				hNum.set(0, hNum.get(0)+1)
			}
			if (model.pattern.robot.map[name].contains(sequence.agent.name)) {
				builder.append(String.format("%s_%s_def = %s_%s(1, %d);\n", model.pattern.name.toString(), sequence.agent.name.toLowerCase(), model.pattern.name.toString(), sequence.agent.name.toLowerCase(), rNum.get(0)))
				rNum.set(0, rNum.get(0)+1)
			}
		]
		
		return builder.toString
	}
	
	// Declaration for the Orchestrators
	def pattDic (Model model) {
		val StringBuilder builder = new StringBuilder 			
		
		builder.append(String.format("%s,\n", model.pattern.name.toString(), model.pattern.name.toString()))
		
		model.sequence.forEach[ sequence |
			if (model.pattern.human.map[name].contains(sequence.agent.name)) {
				builder.append(String.format("%s_%s_def,\n", model.pattern.name.toString(), sequence.agent.name.toLowerCase()))
			}
			if (model.pattern.robot.map[name].contains(sequence.agent.name)) {
				builder.append(String.format("%s_%s_def,\n", model.pattern.name.toString(), sequence.agent.name.toLowerCase()))
			}
		]
		
		// Remove the last comma
		if (builder.length() > 0) {
		    builder.deleteCharAt(builder.length() - 2);
		}
		
		return builder.toString
	}
	
	// Pattern Orchestrator
	def pattOrch (Model model) {
		// Declarations
		val StringBuilder builder = new StringBuilder
		val StringBuilder guard = new StringBuilder
		val StringBuilder update = new StringBuilder
		val patternName = model.pattern.name.toString
		val int[] id = #[0]
		
		val int[] seqNum = #[0]
		val int[] x = #[0]
		val int[] y = #[0]
		val int[] maxIndexHelper = #[0]
		
		builder.append("\t<template>\n")
		builder.append(String.format("\t\t<name>%s_orchestrator</name>\n", patternName));
		builder.append(String.format("\t\t<parameter>int p</parameter>\n"));
		builder.append("\t\t<declaration>// Place local declarations here.\n")
		builder.append("\t\t</declaration>\n")
		
		// Calcola il valore della subsequence più alto
		model.sequence.forEach[sequence |
            sequence.subSequence.forEach[subSequence |
                // Aggiorna maxIndex se necessario
                if (subSequence.i > maxIndexHelper.get(0)) {
                    maxIndexHelper.set(0, subSequence.i)
                }
                if (subSequence.j > maxIndexHelper.get(0)) {
                    maxIndexHelper.set(0, subSequence.j)
                }
            ]
        ]
		
		// GENERA TUTTE LE LOCATION PER OGNI SUBSEQUENCE
		// Init Location
		builder.append(genLoc(id.get(0), x.get(0), y.get(0), "Init", null, null, false, false))
		id.set(0, id.get(0)+1)
			
		for (i : 1..maxIndexHelper.get(0)+1) {
			if (i <= maxIndexHelper.get(0)) {
				builder.append(genLoc(id.get(0), id.get(0)*500, id.get(0)*0, String.format("sub_seq_s%d", id.get(0)), null, null, false, false))
				id.set(0, id.get(0)+1)
			}else{
				builder.append(genLoc(id.get(0), id.get(0)*500, id.get(0)*0, "end", null, null, false, false))
				id.set(0, id.get(0)+1)
			}
        }
        
        // Check the sequence number and save it to the variable seqNum
        model.sequence.forEach[sequence |
        	seqNum.set(0, seqNum.get(0)+1)
        ]
		
		// Define the initial state of the automa
		builder.append(String.format("\t\t<init ref=\"id%d\"/>\n", 0));
		
		// ==========================
		
		val int[] continuation = newIntArrayOfSize(seqNum.get(0)*maxIndexHelper.get(0))

		val int[] s = #[0]
		val int[] ss = #[0]
	
		model.sequence.forEach[sequence |
    		sequence.subSequence.forEach[subSequence |
    		if (subSequence.j - subSequence.i > 0) {
    			for (j: (subSequence.i + 1)..(subSequence.j)) {
    				continuation.set((((j-1)*seqNum.get(0))+s.get(0)), 1)	
    			}
    		}
    		ss.set(0, s.get(0)+1)
			]
			s.set(0, s.get(0)+1)
		]
		
		s.set(0, 0)
		ss.set(0, 0)
		guard.setLength(0);
		
		for (i : 0..maxIndexHelper.get(0)-1) {
			for (j : 0..seqNum.get(0)-1) {
				if (continuation.get(i*seqNum.get(0)+j) == 0) {
					
					model.sequence.forEach[sequence |
			        	if (j == s.get(0) && guard.toString ==  "") {
			        		guard.append(sequence.agent.name.toLowerCase())
			        		guard.append("_stat==true")
			        		update.append(sequence.agent.name.toLowerCase())
			        		update.append("_stat=false")
			        	}else if (j == s.get(0)) {
			        		guard.append(" and ")
			        		guard.append(sequence.agent.name.toLowerCase())
			        		guard.append("_stat==true")
			        		update.append(", ")
			        		update.append(sequence.agent.name.toLowerCase())
			        		update.append("_stat=false")
			        	}
			        	s.set(0, s.get(0)+1)
			        ]
			        s.set(0, 0)
				}
			}
			if (i==0) {
				guard.append(" and p==currP")
			}
			builder.append(genTran(id.get(0), i, i+1, null, guard.toString, String.format("s%d!", i+1), update.toString, true, 500, 0, 0 ,0))
			id.set(0, id.get(0)+1)
			guard.setLength(0);
			update.setLength(0);
			
			if (i==maxIndexHelper.get(0)-1) {
				model.sequence.forEach[sequence |
		        	if (guard.toString ==  "") {
		        		guard.append(sequence.agent.name.toLowerCase())
		        		guard.append("_stat==true")
		        	}else{
		        		guard.append(" and ")
		        		guard.append(sequence.agent.name.toLowerCase())
		        		guard.append("_stat==true")
		        	}
		        	s.set(0, s.get(0)+1)
		        ]
		        update.append("currP=currP+1")
				builder.append(genTran(id.get(0), i+1, i+2, null, guard.toString, String.format("e!"), update.toString, true, 500, 0, 0 ,0))
				id.set(0, id.get(0)+1)
				guard.setLength(0);
				update.setLength(0);
			}
			
		}
		
		// ==========================

		builder.append("\t</template>\n")
		return builder.toString
	}
	
	
// ==========================
// ==========================
// ==========================
// ==========================	
	
		
// Sequence Orchestrators
def seqOrch (Model model) {
	// Declaration
	val StringBuilder transition = new StringBuilder
	val StringBuilder builder = new StringBuilder
	val StringBuilder guard = new StringBuilder
	val StringBuilder update = new StringBuilder
	val StringBuilder sync = new StringBuilder
	val int[] id = #[0]
	val int[] AgentID = #[0]
	val int[] actionID = #[0]
	val int[] locNum = #[0]
	val int[] resNum = #[0]
	val int[] x = #[0]
	val int[] y = #[0]
	val int[] maxIndex = #[0]
	val int[] endID = #[0]
	val int xOffset = 200
	val int yOffset = 350
	val int[] actionCount = #[0]

	// Higher subSequence value
	model.sequence.forEach[sequence |
        sequence.subSequence.forEach[subSequence |
            // Aggiorna maxIndex se necessario
            if (subSequence.i > maxIndex.get(0)) {
                maxIndex.set(0, subSequence.i)
            }
            if (subSequence.j > maxIndex.get(0)) {
                maxIndex.set(0, subSequence.j)
            }
        ]
    ]
    
    // Generate the array that contains the locations
    model.pattern.location.forEach[ loc |
    	locNum.set(0, locNum.get(0)+1)
    ]
    val String[] locations = newArrayOfSize(locNum.get(0))
    locNum.set(0, 0)
	model.pattern.location.forEach[ loc |
    	locations.set(locNum.get(0), loc.name.toString())
    	locNum.set(0, locNum.get(0)+1)
    ]
    
    // Generate the array that contains the resources
    model.pattern.resources.forEach[ res |
    	resNum.set(0, resNum.get(0)+1)
    ]
    val String[] resources = newArrayOfSize(resNum.get(0))
    resNum.set(0, 0)
	model.pattern.resources.forEach[ res |
    	resources.set(resNum.get(0), res.name.toString())
    	resNum.set(0, resNum.get(0)+1)
    ]
    
	// Declarations 
	val int[] continuation = newIntArrayOfSize(maxIndex.get(0))		
	val int[] emptySS = newIntArrayOfSize(maxIndex.get(0))
	val int[] firstActionID = newIntArrayOfSize(maxIndex.get(0)+1) // id of the first action for each subsequence
	val int[] lastActionID = newIntArrayOfSize(maxIndex.get(0)+1) // id of the first action for each subsequence
	val int[] endSubSeqID = newIntArrayOfSize(maxIndex.get(0)+1) // id of the first action for each subsequence
	
	
	model.sequence.forEach[sequence |
        id.set(0, 0)
        x.set(0, 0)
        y.set(0, 0)
        AgentID.set(0, AgentID.get(0)+1)
        actionID.set(0, 1)
        // Search for continuations
        sequence.subSequence.forEach[subSequence |
			if (subSequence.j - subSequence.i > 0) {
				for (j : (subSequence.i + 1)..(subSequence.j)) {
					continuation.set(j-1, 1)	
				}
			}
		]
		
        // ==========================================================
        // ********* SEQUENCE ORCHESTRATOR LOCATIONS ***************
		// ==========================================================
        
        builder.append("\t<template>\n")
		builder.append(String.format("\t\t<name>%s_%s</name>\n", model.pattern.name, sequence.agent.name.toLowerCase()));			
		// The sequence is for a human?
		if (model.pattern.human.map[name].contains(sequence.agent.name)) {
			builder.append(String.format("\t\t<parameter>int p, int H</parameter>\n"));
		}
		// The sequence is for a robot?
		if (model.pattern.robot.map[name].contains(sequence.agent.name)) {
			builder.append(String.format("\t\t<parameter>int p, int R</parameter>\n"));
		}
		
		builder.append("\t\t<declaration>// Place local declarations here.\nclock t;")
		builder.append("\t\t</declaration>\n")
        
        // Initial location for the sequence
        builder.append(genLoc(id.get(0), x.get(0), y.get(0), String.format("init_%s", sequence.agent.name.toLowerCase()), null, null, false, false))
		id.set(0, id.get(0)+1)
		
        // Initial location for each subSequence
        for (i : 1..maxIndex.get(0)) {
        	y.set(0, y.get(0)+yOffset)
        	x.set(0, 0)
        	
        	if (continuation.get(i-1) == 0) {
            	builder.append(genLoc(id.get(0), x.get(0), y.get(0), String.format("s%d_start", i), null, null, false, true))
        	}
			id.set(0, id.get(0)+1)            		
			continuation.set(i-1, 0)
			
			if (i == maxIndex.get(0)) {
				y.set(0, y.get(0)+yOffset)
        		x.set(0, 0)
				builder.append(genLoc(id.get(0), x.get(0), y.get(0), String.format("end_%s", sequence.agent.name.toLowerCase()), null, null, false, false))
				endID.set(0, id.get(0))
				id.set(0, id.get(0)+1)
			}
        }
        
        y.set(0, 0)
        sequence.subSequence.forEach[subSequence |
            // In case of continuation we don'w want idle locations (except for an empty continuation)
            if (subSequence.j !== 0) {
            	for (i : subSequence.i+1..subSequence.j) {
            		emptySS.set(i-1, 1)
            	}
            }
            x.set(0, 0)
            y.set(0, subSequence.i * yOffset)
        	subSequence.actionC.forEach[actionC	|
        		emptySS.set(subSequence.i-1, 1) // The subSequence is not empty

        		// Action busy locations
        		// The agent doing the action is a Human? => Put the invariant t <= 5 for the start iteration
				if (model.pattern.human.map[name].contains(sequence.agent.name)) {
            		x.set(0, x.get(0)+xOffset)
					builder.append(genLoc(id.get(0), x.get(0), y.get(0), String.format("A%d_a%d_%s_busy", AgentID.get(0), actionID.get(0), actionC.actionType.toString()), "t&lt;=5", null, false, false))
				}else{ // The action is done by a robot = no invariant
            		x.set(0, x.get(0)+xOffset)
					builder.append(genLoc(id.get(0), x.get(0), y.get(0), String.format("A%d_a%d_%s_busy", AgentID.get(0), actionID.get(0), actionC.actionType.toString()), null, null, false, false))
				}
				
				// Save the id of the first busy action for each SubSequence
        		if (x.get(0) == xOffset) {
        			firstActionID.set(subSequence.i, id.get(0))
        		}
        		id.set(0, id.get(0)+1)
        		
				// Action complete
				x.set(0, x.get(0)+xOffset)
				builder.append(genLoc(id.get(0), x.get(0), y.get(0), String.format("A%d_a%d_%s_complete", AgentID.get(0), actionID.get(0), actionC.actionType.toString()), null, null, false, true))
        		lastActionID.set(subSequence.i, id.get(0))
				id.set(0, id.get(0)+1)
				actionID.set(0, actionID.get(0)+1)
			]
			
		
			
			// ==============
			// Location and transition for the conditional statement
			// ==============
			actionCount.set(0, 0)
			subSequence.actionC.forEach[actionC	|
				actionCount.set(0, actionCount.get(0)+1)
				// Check conditional statement and add location / transition when necessary
			    if (!actionC.conditionalStatement.isEmpty) {
			    	actionC.conditionalStatement.forEach [cs |
			            // Create the guard accordingly to the atomic predicate
						cs.condition.forEach [c |
							val pred0 = c.atomicPredicate.get(0)
					        if ( pred0.ap == 'position'){
					        	if (pred0.negated) {
					        		guard.append("not (" + apPositionGuard(model, pred0.agent.name.toString(), pred0.location.name.toString()) + ")")
					            } else {
					                guard.append("(" + apPositionGuard(model, pred0.agent.name.toString(), pred0.location.name.toString()) + ")")
					            }
					        }
					        
					        if (c.atomicPredicate.size == 1) {
					            
					        } else {
					            // If more AtomicPredicate, loop on && and ||
					            for (i : 0 ..< (c.atomicPredicate.size - 1)) {
					                val pred2 = c.atomicPredicate.get(i + 1)
					                val operatore = c.op.get(i)
					
					                // Stampa operatore tra predicati
					                if (operatore.toString() == '&&'){
					                	guard.append(" and(")
					                }else if (operatore.toString() == '||'){
					                	guard.append(" or(")
					                }
					                // Controlla se il predicato è negato con '!'
					                if (pred2.negated) {
					                    guard.append("
not (" + apPositionGuard(model, pred2.agent.name.toString(), pred2.location.name.toString()) + "))")
					                } else {
					                    guard.append("
(" + apPositionGuard(model, pred2.agent.name.toString(), pred2.location.name.toString()) + "))")
					                }
					
					            }
					        }
					    ]
			            // Create the shync accordingly to the conditioned action 
			            // The sequence is for a human?
						if (model.pattern.human.map[name].contains(sequence.agent.name)) {
							sync.append("_h_" + actionC.actionType.toString() + "[H-1]!")
						}
						// The sequence is for a robot?
						if (model.pattern.robot.map[name].contains(sequence.agent.name)) {
							sync.append("_r_" + actionC.actionType.toString() + "[R-1]!")
						}
						
						// Create the update accordingly to the original action
						// The agent doing the action is a Human?
						if (model.pattern.human.map[name].contains(sequence.agent.name)) {
							// Check if the Human will do a moveTo
							if (actionC.actionType.toString() == 'moveTo'){
								// We need to check if the target of the action is a location or a resource
								var targetName = actionC.target.name.toString()
								var targetIndex = -1
								
								// 1. Check if target is a Location
								var locIndex = locations.indexOf(targetName)
								if (locIndex != -1) {
									targetIndex = locIndex
								} else {
									// 2. Check if target is a Resource
									var resIndex = resources.indexOf(targetName)
									if (resIndex != -1) {
										// Index = Number of Locations + Resource Index
										targetIndex = locations.length + resIndex
									}
								}
								
								// Only append if a valid target was found
								if (targetIndex != -1) {
						        	update.append(String.format("human_loc_target[H-1] = %d", targetIndex));
						        }
							}
						}
						// The agent doing the action is a Robot?
						if (model.pattern.robot.map[name].contains(sequence.agent.name)) {
							// The action is a moveTo?
							if (actionC.actionType.toString() == 'moveTo'){
						        var targetName = actionC.target.name.toString()
								var targetIndex = -1
								
								// 1. Check if target is a Location
								var locIndex = locations.indexOf(targetName)
								if (locIndex != -1) {
									targetIndex = locIndex
								} else {
									// 2. Check if target is a Resource
									var resIndex = resources.indexOf(targetName)
									if (resIndex != -1) {
										// Index = Number of Locations + Resource Index
										targetIndex = locations.length + resIndex
									}
								}

								// Only append if a valid target was found
								if (targetIndex != -1) {
						        	update.append(String.format("robot_loc_target[R-1] = %d", targetIndex));
						        }
							}
							
						    // The action is a "follow"
						    if (actionC.actionType.toString() == "follow") {
						    	update.append(String.format("f_loc_target[R-1] = %d,\n", locations.indexOf(actionC.target.name.toString())))
								if (model.pattern.human.map[name].contains(actionC.agent.name)) { // We have to follow a human
									update.append(String.format("human_to_be_followed[R-1] = %d,\n", model.pattern.human.map[name].indexOf(actionC.agent.name)+1)) // -1 is done insede the follow action
									update.append("robot_to_be_followed[R-1] = 0")
								}
								if (model.pattern.robot.map[name].contains(actionC.agent.name)) { // We have to follow a robot
									update.append(String.format("robot_to_be_followed[R-1] = %d,\n", model.pattern.robot.map[name].indexOf(actionC.agent.name)+1)) // -1 is done insede the follow action
									update.append("human_to_be_followed[R-1] = 0")
								}
						    }
						    
						    // The action is a pick?
						    if (actionC.actionType.toString() == 'pick'){
						        update.append(String.format("robot_res_to_pick[R-1] = %d, old_inventory = robot_inventory", resources.indexOf(actionC.resource.name.toString())));
							}
							
							// The action is a deposit?
						    if (actionC.actionType.toString() == 'deposit'){
						        update.append(String.format("robot_res_to_deposit[R-1] = %d, old_inventory = robot_inventory", resources.indexOf(actionC.resource.name.toString())));
							}
						}
						
						
			            if (cs.cs1 !== null) { // until
			                // Add the transition from the action to the alternative action
							transition.append(genTran(id.get(0), firstActionID.get(subSequence.i) + actionCount.get(0) * 2 - 2, firstActionID.get(subSequence.i) + actionCount.get(0) * 2 - 1, null, guard.toString(), "stop" + sync.toString(), null, true, xOffset * (actionCount.get(0) * 2-2) + xOffset, subSequence.i * yOffset-100, xOffset * (actionCount.get(0) * 2-2) + xOffset + 100, subSequence.i * yOffset - 100))
							id.set(0, id.get(0)+1)
			            }
			            if (cs.cs2 !== null) { // if/else
			            	// Check the type of the alternative action
			            	// =================== Stop ===================
			            	if (cs.action.actionType.toString() == "stop"){
				                
				                // Generate the location for the stop alternative action
				                builder.append(genLoc(id.get(0), actionCount.get(0)*xOffset*2-xOffset, subSequence.i*yOffset-(yOffset/2), String.format("A%d_a%d_%s", AgentID.get(0), actionID.get(0), cs.action.actionType.toString()), null, null, false, false))
								id.set(0, id.get(0)+1)
								actionID.set(0, actionID.get(0)+1)
								
								// Choose the guard depending on the condition
								// Add the transition from the action to the alternative action
								transition.append(genTran(id.get(0), firstActionID.get(subSequence.i) + actionCount.get(0) * 2 - 2, id.get(0)-1, null, "not (" + guard.toString() + ")", "stop" + sync.toString(), null, true, xOffset * actionCount.get(0) * 2 + xOffset, subSequence.i * yOffset-100, 0, 0))
								id.set(0, id.get(0)+1)
								
								// Transition from the alternative action to the initializer of the action
								if (actionCount.get(0) == 1){
									transition.append(genTran(id.get(0), id.get(0)-2, subSequence.i, null, guard.toString(), "stop" + sync.toString(), update.toString(), true, xOffset * actionCount.get(0) * 2 + xOffset, subSequence.i * yOffset-100, 0, 0))
									id.set(0, id.get(0)+1)
								}else{
									transition.append(genTran(id.get(0), id.get(0)-2, firstActionID.get(subSequence.i) + actionCount.get(0) * 2 - 3, null, guard.toString(), "stop" + sync.toString(), update.toString(), true, xOffset * actionCount.get(0) * 2 + xOffset, subSequence.i * yOffset-100, 0, 0))
									id.set(0, id.get(0)+1)
								}
			            	}
			            	
			            	// =================== MoveTo ===================
			            	if (cs.action.actionType.toString() == "moveTo"){
				                
			            	}
			            	
			            }
			            if (cs.cs3 !== null) { // if/else/until
			            	builder.append(genLoc(id.get(0), x.get(0)-xOffset, y.get(0)-(yOffset/2), String.format("A%d_a%d_%s", AgentID.get(0), actionID.get(0), cs.action.actionType.toString()), null, null, false, false))
							id.set(0, id.get(0)+1)
							actionID.set(0, actionID.get(0)+1)
			            }
			            if (cs.cs4 !== null) { // wait
			            	
			            }
			            guard.setLength(0)
			            sync.setLength(0)
			            update.setLength(0)
		        	]
			    }
        	]
        	
        	if (emptySS.get(subSequence.i-1) == 1) { // If the SubSequence is not empty
        		// Add the location for the SubSequence end
        		x.set(0, x.get(0)+xOffset)
				builder.append(genLoc(id.get(0), x.get(0), y.get(0), String.format("s%d_end", subSequence.i), null, null, false, false))
				transition.append(genTran(id.get(0)+1, lastActionID.get(subSequence.i), id.get(0), null, null, null, String.format("%s_stat = true", sequence.agent.name.toLowerCase()), true, x.get(0)-xOffset, y.get(0), 0 ,0))
				endSubSeqID.set(subSequence.i, id.get(0))
				id.set(0, id.get(0)+2)
        	}
        	
        ]
        
        for (i : 1..maxIndex.get(0)) {
         	if (emptySS.get(i-1) == 0) {
        		x.set(0, xOffset)
            	y.set(0, i * yOffset)
            	builder.append(genLoc(id.get(0), x.get(0), y.get(0), String.format("S%d_idle", i), null, null, false, false))
				endSubSeqID.set(i, id.get(0))
				id.set(0, id.get(0)+1)
        	} 
        	emptySS.set(i-1, 0)
        }
        
        sequence.subSequence.forEach[subSequence |
			if (subSequence.j - subSequence.i > 0) {
				// The last action for the subASequence in a Continuation is the same of the first one
            	if (subSequence.j !== 0) {
	            	for (i : subSequence.i+1..subSequence.j) {
	            		endSubSeqID.set(i, endSubSeqID.get(i-1))
	            	}
	            }
			}
		]
        
        
        builder.append(String.format("\t\t<init ref=\"id%d\"/>\n", 0));
        
        // ==========================================================
        // ==========================================================
        
        // ********* SEQUENCE ORCHESTRATOR TRANSITION ***************
        
        // ==========================================================
        // ==========================================================
   		
   		

   		val int[] firstAction = #[0]
   		actionCount.set(0,0)
   		
   		// Dictionaries
   		val hMapGuard = new java.util.HashMap<String, String>()
		hMapGuard.put("moveTo", "humanPositionX[H-1] &lt;= location_X[human_loc_target[H-1]]+30 and
humanPositionX[H-1] &gt;= location_X[human_loc_target[H-1]]-30 and
humanPositionY[H-1] &lt;= location_Y[human_loc_target[H-1]]+30 and
humanPositionY[H-1] &gt;= location_Y[human_loc_target[H-1]]-30")

		val rMapGuard = new java.util.HashMap<String, String>()
		rMapGuard.put("moveTo", "robPositionX[R-1] &lt;= location_X[robot_loc_target[R-1]]+30 and
robPositionX[R-1] &gt;= location_X[robot_loc_target[R-1]]-30 and
robPositionY[R-1] &lt;= location_Y[robot_loc_target[R-1]]+30 and
robPositionY[R-1] &gt;= location_Y[robot_loc_target[R-1]]-30")
		rMapGuard.put("followH", "humanPositionX[human_to_be_followed[R-1]-1] &lt;= location_X[f_loc_target[R-1]]+30 and
humanPositionX[human_to_be_followed[R-1]-1] &gt;= location_X[f_loc_target[R-1]]-30 and
humanPositionY[human_to_be_followed[R-1]-1] &lt;= location_Y[f_loc_target[R-1]]+30 and
humanPositionY[human_to_be_followed[R-1]-1] &gt;= location_Y[f_loc_target[R-1]]-30 and
robPositionX[R-1] &gt;= location_X[f_loc_target[R-1]]-30 and
robPositionX[R-1] &lt;= location_X[f_loc_target[R-1]]+30 and
robPositionY[R-1] &gt;= location_Y[f_loc_target[R-1]]-30 and
robPositionY[R-1] &lt;= location_Y[f_loc_target[R-1]]+30")
		rMapGuard.put("followR", "robPositionX[robot_to_be_followed[R-1]] &lt;= location_X[f_loc_target[R-1]]+30 and
robPositionX[robot_to_be_followed[R-1]-1] &gt;= location_X[f_loc_target[R-1]]-30 and
robPositionX[robot_to_be_followed[R-1]-1] &lt;= location_Y[f_loc_target[R-1]]+30 and
robPositionX[robot_to_be_followed[R-1]-1] &gt;= location_Y[f_loc_target[R-1]]-30 and
robPositionX[R-1] &gt;= location_X[f_loc_target[R-1]]-30 and
robPositionX[R-1] &lt;= location_X[f_loc_target[R-1]]+30 and
robPositionY[R-1] &gt;= location_Y[f_loc_target[R-1]]-30 and
robPositionY[R-1] &lt;= location_Y[f_loc_target[R-1]]+30")
        rMapGuard.put("pick", "robot_inventory[R-1][robot_res_to_pick[R-1]] > old_inventory[R-1][robot_res_to_pick[R-1]]")
        rMapGuard.put("deposit", "robot_inventory[R-1][robot_res_to_deposit[R-1]] &lt; old_inventory[R-1][robot_res_to_deposit[R-1]]")
        
        sequence.subSequence.forEach[subSequence |
            // In case of continuation we don'w want idle locations (except for an empty continuation)
            if (subSequence.j !== 0) {
            	for (i : subSequence.i+1..subSequence.j) {
            		emptySS.set(i-1, 1)
            	}
            }
            
            firstAction.set(0, 1)
        	
        	subSequence.actionC.forEach[actionC	|
        		emptySS.set(subSequence.i-1, 1) // The subSequence is not empty
        		
				// Transition that brings to a busy action
				if (firstAction.get(0) == 1) {  	// Is it the first busy action of the subSequence
					firstAction.set(0, 0) 		// The next action will not be the first of the subSequence
					
					// The transition to be generated depends on the type of action (for updates and guards)
					if (actionC.actionType.toString() == "moveTo") {
						
						// The agent doing the moveTo is a Human?
						if (model.pattern.human.map[name].contains(sequence.agent.name)) {
							// From current sub-sequence to first busy action
							transition.append(genTran(id.get(0), subSequence.i, firstActionID.get(subSequence.i), null, null, String.format("start_h_moveTo[H-1]!"), "t = 0", true, 0, subSequence.i * yOffset, 0 ,0))
							id.set(0, id.get(0)+1)
							
							// Reach the actual subSequence location
							var targetName = actionC.target.name.toString()
							var targetIndex = -1
							var locIndex = locations.indexOf(targetName)
							if (locIndex != -1) targetIndex = locIndex
							else {
								var resIndex = resources.indexOf(targetName)
								if (resIndex != -1) targetIndex = locations.length + resIndex
							}
							if (targetIndex != -1) {
								transition.append(genTran(id.get(0), endSubSeqID.get(subSequence.i-1), subSequence.i, null, null, String.format("s%d?", subSequence.i), String.format("human_loc_target[H-1] = %d", targetIndex), true, 0, subSequence.i * yOffset - 100, 0 ,0))
							}
							id.set(0, id.get(0)+1)
							
							// From busy to committed that iterate the busy command
							transition.append(genTran(id.get(0), firstActionID.get(subSequence.i), subSequence.i, null, "t&gt;=5", null, "t = 0", true, xOffset * actionCount.get(0) * 2, subSequence.i * yOffset+100, xOffset * actionCount.get(0) * 2+100, subSequence.i * yOffset+80))
							id.set(0, id.get(0)+1)
							
							// From busy to complete
							guard.append(hMapGuard.get("moveTo"))
							
							if (subSequence.actionC.size() >= 2) {
							    // Ottieni l'azione successiva nel subSequence
							    var nextAction = subSequence.actionC.get(subSequence.actionC.indexOf(actionC) + 1);
							    
							    // Verifica se l'azione successiva è di tipo "moveTo"
							    if (nextAction.actionType.toString().equals("moveTo")) {
							        var nextTargetName = nextAction.target.name.toString()
									var nextTargetIndex = -1
									var nextLocIndex = locations.indexOf(nextTargetName)
									if (nextLocIndex != -1) nextTargetIndex = nextLocIndex
									else {
										var nextResIndex = resources.indexOf(nextTargetName)
										if (nextResIndex != -1) nextTargetIndex = locations.length + nextResIndex
									}
									if (nextTargetIndex != -1) {
							        	update.append(String.format("human_loc_target[H-1] = %d", nextTargetIndex));
							        }
							    }
							    // AGGIUNGI AGGIORNAMENTI PER ALTRE AZIONI QUI
							}
							transition.append(genTran(id.get(0), firstActionID.get(subSequence.i), firstActionID.get(subSequence.i)+1, null, guard.toString(), String.format("stop_h_moveTo[H-1]!"), update.toString(), true, xOffset, subSequence.i * yOffset-100, 0 ,0))
							id.set(0, id.get(0)+1)
							guard.setLength(0)
							update.setLength(0)
						}
						
						// The agent doing the moveTo is a Robot?
						if (model.pattern.robot.map[name].contains(sequence.agent.name)) {
							// From current sub-sequence to first busy action
							transition.append(genTran(id.get(0), subSequence.i, firstActionID.get(subSequence.i), null, null, String.format("start_r_moveTo[R-1]!"), null, true, 0, (subSequence.i * yOffset), 0 ,0 ))
							id.set(0, id.get(0)+1)
							
							// Reach the actual subSequence location
							var targetName = actionC.target.name.toString()
							var targetIndex = -1
							var locIndex = locations.indexOf(targetName)
							if (locIndex != -1) targetIndex = locIndex
							else {
								var resIndex = resources.indexOf(targetName)
								if (resIndex != -1) targetIndex = locations.length + resIndex
							}
							if (targetIndex != -1) {
								transition.append(genTran(id.get(0), endSubSeqID.get(subSequence.i-1), subSequence.i, null, null, String.format("s%d?", subSequence.i), String.format("robot_loc_target[R-1] = %d", targetIndex), true, 0, subSequence.i * yOffset - 100, 0 ,0))
							}
							id.set(0, id.get(0)+1)
							// From busy to complete
							guard.append(rMapGuard.get("moveTo"))

							if (subSequence.actionC.indexOf(actionC) < subSequence.actionC.size() - 1) {
							    // Ottieni l'azione successiva nel subSequence
							    var nextAction = subSequence.actionC.get(subSequence.actionC.indexOf(actionC) + 1);
							    
							    // Verifica se l'azione successiva è di tipo "moveTo"
							    if (nextAction.actionType.toString().equals("moveTo")) {
							        var nextTargetName = nextAction.target.name.toString()
									var nextTargetIndex = -1
									var nextLocIndex = locations.indexOf(nextTargetName)
									if (nextLocIndex != -1) nextTargetIndex = nextLocIndex
									else {
										var nextResIndex = resources.indexOf(nextTargetName)
										if (nextResIndex != -1) nextTargetIndex = locations.length + nextResIndex
									}
									if (nextTargetIndex != -1) {
							        	update.append(String.format("robot_loc_target[R-1] = %d", nextTargetIndex));
							        }
							    }
							    
							    // Verifica se l'azione successiva è di tipo "follow"
							    if (nextAction.actionType.toString().equals("follow")) {
							    	update.append(String.format("f_loc_target[R-1] = %d,\n", locations.indexOf(nextAction.target.name.toString())))
									if (model.pattern.human.map[name].contains(nextAction.agent.name)) { // We have to follow a human
										update.append(String.format("human_to_be_followed[R-1] = %d,\n", model.pattern.human.map[name].indexOf(nextAction.agent.name)+1)) // -1 is done insede the follow action
										update.append("robot_to_be_followed[R-1] = 0")
									}
									if (model.pattern.robot.map[name].contains(nextAction.agent.name)) { // We have to follow a robot
										update.append(String.format("robot_to_be_followed[R-1] = %d,\n", model.pattern.robot.map[name].indexOf(nextAction.agent.name)+1)) // -1 is done insede the follow action
										update.append("human_to_be_followed[R-1] = 0")
									}
							    }
							    
							    if (nextAction.actionType.toString().equals("pick")) {
							        update.append(String.format("robot_res_to_pick[R-1] = %d, old_inventory = robot_inventory", resources.indexOf(nextAction.resource.name.toString())));
							    }
							    
							    if (nextAction.actionType.toString().equals("deposit")) {
							        update.append(String.format("robot_res_to_deposit[R-1] = %d, old_inventory = robot_inventory", resources.indexOf(nextAction.resource.name.toString())));
							    }
							    
							    // AGGIUNGI AGGIORNAMENTI PER ALTRE AZIONI QUI (ROBOT)
							}
							transition.append(genTran(id.get(0), firstActionID.get(subSequence.i), firstActionID.get(subSequence.i)+1, null, guard.toString(), String.format("stop_r_moveTo[R-1]!"), update.toString(), true, xOffset, subSequence.i * yOffset-100, 0 ,0))
							id.set(0, id.get(0)+1)
							guard.setLength(0)
							update.setLength(0)
						}
					}
					// FOLLOW (implemented only for robots)
					if (actionC.actionType.toString() == "follow") {
			
						// The agent doing the action is a Robot?
						if (model.pattern.robot.map[name].contains(sequence.agent.name)) {
							
							// From current sub-sequence to first busy action
							transition.append(genTran(id.get(0), subSequence.i, firstActionID.get(subSequence.i), null, null, String.format("start_r_follow[R-1]!"), null, true, 0, (subSequence.i * yOffset), 0 ,0 ))
							id.set(0, id.get(0)+1)
							
							// Reach the actual subSequence location
							update.append(String.format("f_loc_target[R-1] = %d,\n", locations.indexOf(actionC.target.name.toString())))
							if (model.pattern.human.map[name].contains(actionC.agent.name)) { // We have to follow a human
								update.append(String.format("human_to_be_followed[R-1] = %d,\n", model.pattern.human.map[name].indexOf(actionC.agent.name)+1)) // -1 is done insede the follow action
								update.append("robot_to_be_followed[R-1] = 0")
							}
							if (model.pattern.robot.map[name].contains(actionC.agent.name)) { // We have to follow a robot
								update.append(String.format("robot_to_be_followed[R-1] = %d,\n", model.pattern.robot.map[name].indexOf(actionC.agent.name)+1)) // -1 is done insede the follow action
								update.append("human_to_be_followed[R-1] = 0")
							}
							transition.append(genTran(id.get(0), endSubSeqID.get(subSequence.i-1), subSequence.i, null, null, String.format("s%d?", subSequence.i), update.toString(), true, 0, subSequence.i * yOffset - 100, 0 ,0))
							id.set(0, id.get(0)+1)
							update.setLength(0)
							
							// From busy to complete
							if (model.pattern.human.map[name].contains(actionC.agent.name)) { // We have to follow a human
								guard.append(rMapGuard.get("followH"))
							}
							if (model.pattern.robot.map[name].contains(actionC.agent.name)) { // We have to follow a robot
								guard.append(rMapGuard.get("followR"))
							}
							// We have to update variables wrt the next action
							if (subSequence.actionC.indexOf(actionC) < subSequence.actionC.size() - 1) {
							    // Ottieni l'azione successiva nel subSequence
							    var nextAction = subSequence.actionC.get(subSequence.actionC.indexOf(actionC) + 1);
							    
							    // Verifica se l'azione successiva è di tipo "moveTo"
							    if (nextAction.actionType.toString().equals("moveTo")) {
							        var nextTargetName = nextAction.target.name.toString()
									var nextTargetIndex = -1
									var nextLocIndex = locations.indexOf(nextTargetName)
									if (nextLocIndex != -1) nextTargetIndex = nextLocIndex
									else {
										var nextResIndex = resources.indexOf(nextTargetName)
										if (nextResIndex != -1) nextTargetIndex = locations.length + nextResIndex
									}
									if (nextTargetIndex != -1) {
							        	update.append(String.format("robot_loc_target[R-1] = %d", nextTargetIndex));
							        }
							    }
							    
							    // Verifica se l'azione successiva è di tipo "follow"
							    if (nextAction.actionType.toString().equals("follow")) {
							    	update.append(String.format("f_loc_target[R-1] = %d,\n", locations.indexOf(nextAction.target.name.toString())))
									if (model.pattern.human.map[name].contains(nextAction.agent.name)) { // We have to follow a human
										update.append(String.format("human_to_be_followed[R-1] = %d,\n", model.pattern.human.map[name].indexOf(nextAction.agent.name)+1)) // -1 is done insede the follow action
										update.append("robot_to_be_followed[R-1] = 0")
									}
									if (model.pattern.robot.map[name].contains(nextAction.agent.name)) { // We have to follow a robot
										update.append(String.format("robot_to_be_followed[R-1] = %d,\n", model.pattern.robot.map[name].indexOf(nextAction.agent.name)+1)) // -1 is done insede the follow action
										update.append("human_to_be_followed[R-1] = 0")
									}
							    }
							    
							    // Verifica se l'azione successiva è di tipo "pick"
							    if (nextAction.actionType.toString().equals("pick")) {
							        update.append(String.format("robot_res_to_pick[R-1] = %d, old_inventory = robot_inventory", resources.indexOf(nextAction.resource.name.toString())));
							    }
							    
							    // Verifica se l'azione successiva è di tipo "Deposit"
							    if (nextAction.actionType.toString().equals("deposit")) {
							        update.append(String.format("robot_res_to_pdeposit[R-1] = %d, old_inventory = robot_inventory", resources.indexOf(nextAction.resource.name.toString())));
							    }
							    
							    // AGGIUNGI AGGIORNAMENTI PER ALTRE AZIONI QUI (Robot)
							}
							transition.append(genTran(id.get(0), firstActionID.get(subSequence.i), firstActionID.get(subSequence.i)+1, null, guard.toString(), String.format("stop_r_follow[R-1]!"), update.toString(), true, xOffset, subSequence.i * yOffset-100, 0 ,0))
							id.set(0, id.get(0)+1)
							guard.setLength(0)
							update.setLength(0)
						}
					}
					
					// PICK (implemented only for robots)
					if (actionC.actionType.toString() == "pick") {
			
						// The agent doing the action is a Robot?
						if (model.pattern.robot.map[name].contains(sequence.agent.name)) {
							
							// From current sub-sequence to first busy action
							transition.append(genTran(id.get(0), subSequence.i, firstActionID.get(subSequence.i), null, null, String.format("start_r_pick[R-1]!"), null, true, 0, (subSequence.i * yOffset), 0 ,0 ))
							id.set(0, id.get(0)+1)
							
							// Reach the actual subSequence location
							update.append(String.format("robot_res_to_pick[R-1] = %d, old_inventory = robot_inventory", resources.indexOf(actionC.resource.name.toString())))
							
							transition.append(genTran(id.get(0), endSubSeqID.get(subSequence.i-1), subSequence.i, null, null, String.format("s%d?", subSequence.i), update.toString(), true, 0, subSequence.i * yOffset - 100, 0 ,0))
							id.set(0, id.get(0)+1)
							update.setLength(0)
							
							// From busy to complete
							guard.append(rMapGuard.get("pick"))
							
							// We have to update variables wrt the next action
							if (subSequence.actionC.indexOf(actionC) < subSequence.actionC.size() - 1) {
							    // Ottieni l'azione successiva nel subSequence
							    var nextAction = subSequence.actionC.get(subSequence.actionC.indexOf(actionC) + 1);
							    
							    // Verifica se l'azione successiva è di tipo "moveTo"
							    if (nextAction.actionType.toString().equals("moveTo")) {
							        var nextTargetName = nextAction.target.name.toString()
									var nextTargetIndex = -1
									var nextLocIndex = locations.indexOf(nextTargetName)
									if (nextLocIndex != -1) nextTargetIndex = nextLocIndex
									else {
										var nextResIndex = resources.indexOf(nextTargetName)
										if (nextResIndex != -1) nextTargetIndex = locations.length + nextResIndex
									}
									if (nextTargetIndex != -1) {
							        	update.append(String.format("robot_loc_target[R-1] = %d", nextTargetIndex));
							        }
							    }
							    
							    // Verifica se l'azione successiva è di tipo "follow"
							    if (nextAction.actionType.toString().equals("follow")) {
							    	update.append(String.format("f_loc_target[R-1] = %d,\n", locations.indexOf(nextAction.target.name.toString())))
									if (model.pattern.human.map[name].contains(nextAction.agent.name)) { // We have to follow a human
										update.append(String.format("human_to_be_followed[R-1] = %d,\n", model.pattern.human.map[name].indexOf(nextAction.agent.name)+1)) // -1 is done insede the follow action
										update.append("robot_to_be_followed[R-1] = 0")
									}
									if (model.pattern.robot.map[name].contains(nextAction.agent.name)) { // We have to follow a robot
										update.append(String.format("robot_to_be_followed[R-1] = %d,\n", model.pattern.robot.map[name].indexOf(nextAction.agent.name)+1)) // -1 is done insede the follow action
										update.append("human_to_be_followed[R-1] = 0")
									}
							    }
							    
							    // Verifica se l'azione successiva è di tipo "pick"
							    if (nextAction.actionType.toString().equals("pick")) {
							        update.append(String.format("robot_res_to_pick[R-1] = %d, old_inventory = robot_inventory", resources.indexOf(nextAction.resource.name.toString())));
							    }
							    
							    // Verifica se l'azione successiva è di tipo "deposit"
							    if (nextAction.actionType.toString().equals("deposit")) {
							        update.append(String.format("robot_res_to_deposit[R-1] = %d, old_inventory = robot_inventory", resources.indexOf(nextAction.resource.name.toString())));
							    }
							    
							    // AGGIUNGI AGGIORNAMENTI PER ALTRE AZIONI QUI (Robot)
							}
							transition.append(genTran(id.get(0), firstActionID.get(subSequence.i), firstActionID.get(subSequence.i)+1, null, guard.toString(), String.format("stop_r_pick[R-1]!"), update.toString(), true, xOffset, subSequence.i * yOffset-100, 0 ,0))
							id.set(0, id.get(0)+1)
							guard.setLength(0)
							update.setLength(0)
						}
					}
					
					// DEPOSIT (implemented only for robots)
					if (actionC.actionType.toString() == "deposit") {
			
						// The agent doing the action is a Robot?
						if (model.pattern.robot.map[name].contains(sequence.agent.name)) {
							
							// From current sub-sequence to first busy action
							transition.append(genTran(id.get(0), subSequence.i, firstActionID.get(subSequence.i), null, null, String.format("start_r_deposit[R-1]!"), null, true, 0, (subSequence.i * yOffset), 0 ,0 ))
							id.set(0, id.get(0)+1)
							
							// Reach the actual subSequence location
							update.append(String.format("robot_res_to_deposit[R-1] = %d, old_inventory = robot_inventory", resources.indexOf(actionC.resource.name.toString())))
							
							transition.append(genTran(id.get(0), endSubSeqID.get(subSequence.i-1), subSequence.i, null, null, String.format("s%d?", subSequence.i), update.toString(), true, 0, subSequence.i * yOffset - 100, 0 ,0))
							id.set(0, id.get(0)+1)
							update.setLength(0)
							
							// From busy to complete
							guard.append(rMapGuard.get("deposit"))
							
							// We have to update variables wrt the next action
							if (subSequence.actionC.indexOf(actionC) < subSequence.actionC.size() - 1) {
							    // Ottieni l'azione successiva nel subSequence
							    var nextAction = subSequence.actionC.get(subSequence.actionC.indexOf(actionC) + 1);
							    
							    // Verifica se l'azione successiva è di tipo "moveTo"
							    if (nextAction.actionType.toString().equals("moveTo")) {
							        var nextTargetName = nextAction.target.name.toString()
									var nextTargetIndex = -1
									var nextLocIndex = locations.indexOf(nextTargetName)
									if (nextLocIndex != -1) nextTargetIndex = nextLocIndex
									else {
										var nextResIndex = resources.indexOf(nextTargetName)
										if (nextResIndex != -1) nextTargetIndex = locations.length + nextResIndex
									}
									if (nextTargetIndex != -1) {
							        	update.append(String.format("robot_loc_target[R-1] = %d", nextTargetIndex));
							        }
							    }
							    
							    // Verifica se l'azione successiva è di tipo "follow"
							    if (nextAction.actionType.toString().equals("follow")) {
							    	update.append(String.format("f_loc_target[R-1] = %d,\n", locations.indexOf(nextAction.target.name.toString())))
									if (model.pattern.human.map[name].contains(nextAction.agent.name)) { // We have to follow a human
										update.append(String.format("human_to_be_followed[R-1] = %d,\n", model.pattern.human.map[name].indexOf(nextAction.agent.name)+1)) // -1 is done insede the follow action
										update.append("robot_to_be_followed[R-1] = 0")
									}
									if (model.pattern.robot.map[name].contains(nextAction.agent.name)) { // We have to follow a robot
										update.append(String.format("robot_to_be_followed[R-1] = %d,\n", model.pattern.robot.map[name].indexOf(nextAction.agent.name)+1)) // -1 is done insede the follow action
										update.append("human_to_be_followed[R-1] = 0")
									}
							    }
							    
							    // Verifica se l'azione successiva è di tipo "pick"
							    if (nextAction.actionType.toString().equals("pick")) {
							        update.append(String.format("robot_res_to_pick[R-1] = %d, old_inventory = robot_inventory", resources.indexOf(nextAction.resource.name.toString())));
							    }
							    
							    // Verifica se l'azione successiva è di tipo "deposit"
							    if (nextAction.actionType.toString().equals("deposit")) {
							        update.append(String.format("robot_res_to_deposit[R-1] = %d, old_inventory = robot_inventory", resources.indexOf(nextAction.resource.name.toString())));
							    }
							    
							    // AGGIUNGI AGGIORNAMENTI PER ALTRE AZIONI QUI (Robot)
							}
							transition.append(genTran(id.get(0), firstActionID.get(subSequence.i), firstActionID.get(subSequence.i)+1, null, guard.toString(), String.format("stop_r_deposit[R-1]!"), update.toString(), true, xOffset, subSequence.i * yOffset-100, 0 ,0))
							id.set(0, id.get(0)+1)
							guard.setLength(0)
							update.setLength(0)
						}
					}
					
					// ADD TRANSITION FOR EACH ACTION
					
				} else { // Not the first busy action of a subsequence
				
					actionCount.set(0, actionCount.get(0)+1)
					// ADD TRANSITION FOR EACH ACTION
					
					// The transition to be generated depends on the type of action (for updates and guards)
					if (actionC.actionType.toString() == "moveTo") {
						
						// The agent doing the action is a Human?
						if (model.pattern.human.map[name].contains(sequence.agent.name)) {
							// From Last complete to the busy
							transition.append(genTran(id.get(0), firstActionID.get(subSequence.i) + actionCount.get(0) * 2 - 1, firstActionID.get(subSequence.i) + actionCount.get(0) * 2, null, null, String.format("start_h_moveTo[H-1]!"), "t = 0", true, xOffset * actionCount.get(0) * 2, subSequence.i * yOffset, 0 ,0))
							id.set(0, id.get(0)+1)
							
							// From busy to committed that iterate the busy command
							transition.append(genTran(id.get(0), firstActionID.get(subSequence.i) + actionCount.get(0) * 2, firstActionID.get(subSequence.i) + actionCount.get(0) * 2 - 1, null, "t&gt;=5", null, "t = 0", true, xOffset * actionCount.get(0) * 2, subSequence.i * yOffset+100, xOffset * actionCount.get(0) * 2+100, subSequence.i * yOffset+80))
							id.set(0, id.get(0)+1)
							
							
							// From busy to complete
							guard.append(hMapGuard.get("moveTo"))
							
							if (subSequence.actionC.size() >= actionCount.get(0) + 2) {
							    // Ottieni l'azione successiva nel subSequence
							    var nextAction = subSequence.actionC.get(subSequence.actionC.indexOf(actionC) + 1);
							    
							    // Verifica se l'azione successiva è di tipo "moveTo"
							    if (nextAction.actionType.toString().equals("moveTo")) {
							        var nextTargetName = nextAction.target.name.toString()
									var nextTargetIndex = -1
									var nextLocIndex = locations.indexOf(nextTargetName)
									if (nextLocIndex != -1) nextTargetIndex = nextLocIndex
									else {
										var nextResIndex = resources.indexOf(nextTargetName)
										if (nextResIndex != -1) nextTargetIndex = locations.length + nextResIndex
									}
									if (nextTargetIndex != -1) {
							        	update.append(String.format("human_loc_target[R-1] = %d", nextTargetIndex));
							        }
							    }
							    // AGGIUNGI AGGIORNAMENTI PER ALTRE AZIONI QUI
							}
							transition.append(genTran(id.get(0), firstActionID.get(subSequence.i) + actionCount.get(0) * 2, firstActionID.get(subSequence.i) + actionCount.get(0) * 2 + 1, null, guard.toString(), String.format("stop_h_moveTo[H-1]!"), update.toString(), true, xOffset * (actionCount.get(0) * 2 +1), subSequence.i * yOffset-100, 0 ,0))
							id.set(0, id.get(0)+1)
							guard.setLength(0)
							update.setLength(0)
						}
						
						// The agent doing the action is a Robot?
						if (model.pattern.robot.map[name].contains(sequence.agent.name)) {
							// From Last complete to the busy
							transition.append(genTran(id.get(0), firstActionID.get(subSequence.i) + actionCount.get(0) * 2 - 1, firstActionID.get(subSequence.i) + actionCount.get(0) * 2, null, null, String.format("start_r_moveTo[R-1]!"), null, true, xOffset * actionCount.get(0) * 2, subSequence.i * yOffset, 0 ,0))
							id.set(0, id.get(0)+1)
							
							// From busy to complete
							guard.append(rMapGuard.get("moveTo"))
							
							if (subSequence.actionC.size() >= actionCount.get(0) + 2) {
							    // Ottieni l'azione successiva nel subSequence
							    var nextAction = subSequence.actionC.get(subSequence.actionC.indexOf(actionC) + 1);
							    
							    // Verifica se l'azione successiva è di tipo "moveTo"
							    if (nextAction.actionType.toString().equals("moveTo")) {
							        var nextTargetName = nextAction.target.name.toString()
									var nextTargetIndex = -1
									var nextLocIndex = locations.indexOf(nextTargetName)
									if (nextLocIndex != -1) nextTargetIndex = nextLocIndex
									else {
										var nextResIndex = resources.indexOf(nextTargetName)
										if (nextResIndex != -1) nextTargetIndex = locations.length + nextResIndex
									}
									if (nextTargetIndex != -1) {
							        	update.append(String.format("robot_loc_target[R-1] = %d", nextTargetIndex));
							        }
							    }
							    
							    // Verifica se l'azione successiva è di tipo "follow"
							    if (nextAction.actionType.toString().equals("follow")) {
							    	update.append(String.format("f_loc_target[R-1] = %d,\n", locations.indexOf(nextAction.target.name.toString())))
									if (model.pattern.human.map[name].contains(nextAction.agent.name)) { // We have to follow a human
										update.append(String.format("human_to_be_followed[R-1] = %d,\n", model.pattern.human.map[name].indexOf(nextAction.agent.name)+1)) // -1 is done insede the follow action
										update.append("robot_to_be_followed[R-1] = 0")
									}
									if (model.pattern.robot.map[name].contains(nextAction.agent.name)) { // We have to follow a robot
										update.append(String.format("robot_to_be_followed[R-1] = %d,\n", model.pattern.robot.map[name].indexOf(nextAction.agent.name)+1)) // -1 is done insede the follow action
										update.append("human_to_be_followed[R-1] = 0")
									}
							    }
							    
							    // Verifica se l'azione successiva è di tipo "pick"
							    if (nextAction.actionType.toString().equals("pick")) {
							        update.append(String.format("robot_res_to_pick[R-1] = %d, old_inventory = robot_inventory", resources.indexOf(nextAction.resource.name.toString())));
							    }
							    
							    // Verifica se l'azione successiva è di tipo "deposit"
							    if (nextAction.actionType.toString().equals("deposit")) {
							        update.append(String.format("robot_res_to_deposit[R-1] = %d, old_inventory = robot_inventory", resources.indexOf(nextAction.resource.name.toString())));
							    }
							    
							    // AGGIUNGI AGGIORNAMENTI PER ALTRE AZIONI QUI
							}
							transition.append(genTran(id.get(0), firstActionID.get(subSequence.i) + actionCount.get(0) * 2, firstActionID.get(subSequence.i) + actionCount.get(0) * 2 + 1, null, guard.toString(), String.format("stop_r_moveTo[R-1]!"), update.toString(), true, xOffset * (actionCount.get(0) * 2 +1), subSequence.i * yOffset-100, 0 ,0))
							id.set(0, id.get(0)+1)
							guard.setLength(0)
							update.setLength(0)
						}
					}
					
					// Follow - Not as first action
					if (actionC.actionType.toString() == "follow") {
						
						// The agent doing the follow is a Robot?
						if (model.pattern.robot.map[name].contains(sequence.agent.name)) {
							
							// From Last complete to the busy
							transition.append(genTran(id.get(0), firstActionID.get(subSequence.i) + actionCount.get(0) * 2 - 1, firstActionID.get(subSequence.i) + actionCount.get(0) * 2, null, null, String.format("start_r_follow[R-1]!"), null, true, xOffset * actionCount.get(0) * 2, subSequence.i * yOffset, 0 ,0))
							id.set(0, id.get(0)+1)
							
							// From busy to complete
							if (model.pattern.human.map[name].contains(actionC.agent.name)) { // We have to follow a human
								guard.append(rMapGuard.get("followH"))
							}
							if (model.pattern.robot.map[name].contains(actionC.agent.name)) { // We have to follow a robot
								guard.append(rMapGuard.get("followR"))
							}
							// We have to update variables wrt the next action
							if (subSequence.actionC.indexOf(actionC) < subSequence.actionC.size() - 1) {
							    // Ottieni l'azione successiva nel subSequence
							    var nextAction = subSequence.actionC.get(subSequence.actionC.indexOf(actionC) + 1);
							    
							    // Verifica se l'azione successiva è di tipo "moveTo"
							    if (nextAction.actionType.toString().equals("moveTo")) {
							    	// FIX TARGET INDEX CALCULATION (LOOKAHEAD)
							        var nextTargetName = nextAction.target.name.toString()
									var nextTargetIndex = -1
									var nextLocIndex = locations.indexOf(nextTargetName)
									if (nextLocIndex != -1) nextTargetIndex = nextLocIndex
									else {
										var nextResIndex = resources.indexOf(nextTargetName)
										if (nextResIndex != -1) nextTargetIndex = locations.length + nextResIndex
									}
									if (nextTargetIndex != -1) {
							        	update.append(String.format("robot_loc_target[R-1] = %d", nextTargetIndex));
							        }
							    }
							    
							    // Verifica se l'azione successiva è di tipo "follow"
							    if (nextAction.actionType.toString().equals("follow")) {
							    	update.append(String.format("f_loc_target[R-1] = %d,\n", locations.indexOf(nextAction.target.name.toString())))
									if (model.pattern.human.map[name].contains(nextAction.agent.name)) { // We have to follow a human
										update.append(String.format("human_to_be_followed[R-1] = %d,\n", model.pattern.human.map[name].indexOf(nextAction.agent.name)+1)) // -1 is done insede the follow action
										update.append("robot_to_be_followed[R-1] = 0")
									}
									if (model.pattern.robot.map[name].contains(nextAction.agent.name)) { // We have to follow a robot
										update.append(String.format("robot_to_be_followed[R-1] = %d,\n", model.pattern.robot.map[name].indexOf(nextAction.agent.name)+1)) // -1 is done insede the follow action
										update.append("human_to_be_followed[R-1] = 0")
									}
							    }
							   	
							   	// Verifica se l'azione successiva è di tipo "pick"
							    if (nextAction.actionType.toString().equals("pick")) {
							        update.append(String.format("robot_res_to_pick[R-1] = %d, old_inventory = robot_inventory", resources.indexOf(nextAction.resource.name.toString())));
							    }
							    
							    // Verifica se l'azione successiva è di tipo "deposit"
							    if (nextAction.actionType.toString().equals("deposit")) {
							        update.append(String.format("robot_res_to_deposit[R-1] = %d, old_inventory = robot_inventory", resources.indexOf(nextAction.resource.name.toString())));
							    }
							    
							    // AGGIUNGI AGGIORNAMENTI PER ALTRE AZIONI QUI
							}
							transition.append(genTran(id.get(0), firstActionID.get(subSequence.i) + actionCount.get(0) * 2, firstActionID.get(subSequence.i) + actionCount.get(0) * 2 + 1, null, guard.toString(), String.format("stop_r_follow[R-1]!"), update.toString(), true, xOffset * (actionCount.get(0) * 2 +1), subSequence.i * yOffset-100, 0 ,0))
							id.set(0, id.get(0)+1)
							guard.setLength(0)
							update.setLength(0)
						}
					}
					// Pick - Not as first action
					if (actionC.actionType.toString() == "pick") {
						
						// The agent doing the pick is a Robot?
						if (model.pattern.robot.map[name].contains(sequence.agent.name)) {
							
							// From Last complete to the busy
							transition.append(genTran(id.get(0), firstActionID.get(subSequence.i) + actionCount.get(0) * 2 - 1, firstActionID.get(subSequence.i) + actionCount.get(0) * 2, null, null, String.format("start_r_pick[R-1]!"), null, true, xOffset * actionCount.get(0) * 2, subSequence.i * yOffset, 0 ,0))
							id.set(0, id.get(0)+1)
							
							// From busy to complete
							guard.append(rMapGuard.get("pick"))
							
							// We have to update variables wrt the next action
							if (subSequence.actionC.indexOf(actionC) < subSequence.actionC.size() - 1) {
							    // Ottieni l'azione successiva nel subSequence
							    var nextAction = subSequence.actionC.get(subSequence.actionC.indexOf(actionC) + 1);
							    
							    // Verifica se l'azione successiva è di tipo "moveTo"
							    if (nextAction.actionType.toString().equals("moveTo")) {
							        var nextTargetName = nextAction.target.name.toString()
									var nextTargetIndex = -1
									var nextLocIndex = locations.indexOf(nextTargetName)
									if (nextLocIndex != -1) nextTargetIndex = nextLocIndex
									else {
										var nextResIndex = resources.indexOf(nextTargetName)
										if (nextResIndex != -1) nextTargetIndex = locations.length + nextResIndex
									}
									if (nextTargetIndex != -1) {
							        	update.append(String.format("robot_loc_target[R-1] = %d", nextTargetIndex));
							        }
							    }
							    
							    // Verifica se l'azione successiva è di tipo "follow"
							    if (nextAction.actionType.toString().equals("follow")) {
							    	update.append(String.format("f_loc_target[R-1] = %d,\n", locations.indexOf(nextAction.target.name.toString())))
									if (model.pattern.human.map[name].contains(nextAction.agent.name)) { // We have to follow a human
										update.append(String.format("human_to_be_followed[R-1] = %d,\n", model.pattern.human.map[name].indexOf(nextAction.agent.name)+1)) // -1 is done insede the follow action
										update.append("robot_to_be_followed[R-1] = 0")
									}
									if (model.pattern.robot.map[name].contains(nextAction.agent.name)) { // We have to follow a robot
										update.append(String.format("robot_to_be_followed[R-1] = %d,\n", model.pattern.robot.map[name].indexOf(nextAction.agent.name)+1)) // -1 is done insede the follow action
										update.append("human_to_be_followed[R-1] = 0")
									}
							    }
							   	
							   	// Verifica se l'azione successiva è di tipo "pick"
							    if (nextAction.actionType.toString().equals("pick")) {
							        update.append(String.format("robot_res_to_pick[R-1] = %d, old_inventory = robot_inventory", resources.indexOf(nextAction.resource.name.toString())));
							    }
							    
							    // Verifica se l'azione successiva è di tipo "deposit"
							    if (nextAction.actionType.toString().equals("deposit")) {
							        update.append(String.format("robot_res_to_deposit[R-1] = %d, old_inventory = robot_inventory", resources.indexOf(nextAction.resource.name.toString())));
							    }
							    
							    // AGGIUNGI AGGIORNAMENTI PER ALTRE AZIONI QUI
							}
							transition.append(genTran(id.get(0), firstActionID.get(subSequence.i) + actionCount.get(0) * 2, firstActionID.get(subSequence.i) + actionCount.get(0) * 2 + 1, null, guard.toString(), String.format("stop_r_pick[R-1]!"), update.toString(), true, xOffset * (actionCount.get(0) * 2 +1), subSequence.i * yOffset-100, 0 ,0))
							id.set(0, id.get(0)+1)
							guard.setLength(0)
							update.setLength(0)
						}
					}
					
					// Deposit - Not as first action
					if (actionC.actionType.toString() == "deposit") {
						
						// The agent doing the deposit is a Robot?
						if (model.pattern.robot.map[name].contains(sequence.agent.name)) {
							
							// From Last complete to the busy
							transition.append(genTran(id.get(0), firstActionID.get(subSequence.i) + actionCount.get(0) * 2 - 1, firstActionID.get(subSequence.i) + actionCount.get(0) * 2, null, null, String.format("start_r_deposit[R-1]!"), null, true, xOffset * actionCount.get(0) * 2, subSequence.i * yOffset, 0 ,0))
							id.set(0, id.get(0)+1)
							
							// From busy to complete
							guard.append(rMapGuard.get("deposit"))
							
							// We have to update variables wrt the next action
							if (subSequence.actionC.indexOf(actionC) < subSequence.actionC.size() - 1) {
							    // Ottieni l'azione successiva nel subSequence
							    var nextAction = subSequence.actionC.get(subSequence.actionC.indexOf(actionC) + 1);
							    
							    // Verifica se l'azione successiva è di tipo "moveTo"
							    if (nextAction.actionType.toString().equals("moveTo")) {
							        var nextTargetName = nextAction.target.name.toString()
									var nextTargetIndex = -1
									var nextLocIndex = locations.indexOf(nextTargetName)
									if (nextLocIndex != -1) nextTargetIndex = nextLocIndex
									else {
										var nextResIndex = resources.indexOf(nextTargetName)
										if (nextResIndex != -1) nextTargetIndex = locations.length + nextResIndex
									}
									if (nextTargetIndex != -1) {
							        	update.append(String.format("robot_loc_target[R-1] = %d", nextTargetIndex));
							        }
							    }
							    
							    // Verifica se l'azione successiva è di tipo "follow"
							    if (nextAction.actionType.toString().equals("follow")) {
							    	update.append(String.format("f_loc_target[R-1] = %d,\n", locations.indexOf(nextAction.target.name.toString())))
									if (model.pattern.human.map[name].contains(nextAction.agent.name)) { // We have to follow a human
										update.append(String.format("human_to_be_followed[R-1] = %d,\n", model.pattern.human.map[name].indexOf(nextAction.agent.name)+1)) // -1 is done insede the follow action
										update.append("robot_to_be_followed[R-1] = 0")
									}
									if (model.pattern.robot.map[name].contains(nextAction.agent.name)) { // We have to follow a robot
										update.append(String.format("robot_to_be_followed[R-1] = %d,\n", model.pattern.robot.map[name].indexOf(nextAction.agent.name)+1)) // -1 is done insede the follow action
										update.append("human_to_be_followed[R-1] = 0")
									}
							    }
							   	
							   	// Verifica se l'azione successiva è di tipo "pick"
							    if (nextAction.actionType.toString().equals("pick")) {
							        update.append(String.format("robot_res_to_pick[R-1] = %d, old_inventory = robot_inventory", resources.indexOf(nextAction.resource.name.toString())));
							    }
							    
							    // Verifica se l'azione successiva è di tipo "deposit"
							    if (nextAction.actionType.toString().equals("deposit")) {
							        update.append(String.format("robot_res_to_deposit[R-1] = %d, old_inventory = robot_inventory", resources.indexOf(nextAction.resource.name.toString())));
							    }
							    
							    // AGGIUNGI AGGIORNAMENTI PER ALTRE AZIONI QUI
							}
							transition.append(genTran(id.get(0), firstActionID.get(subSequence.i) + actionCount.get(0) * 2, firstActionID.get(subSequence.i) + actionCount.get(0) * 2 + 1, null, guard.toString(), String.format("stop_r_deposit[R-1]!"), update.toString(), true, xOffset * (actionCount.get(0) * 2 +1), subSequence.i * yOffset-100, 0 ,0))
							id.set(0, id.get(0)+1)
							guard.setLength(0)
							update.setLength(0)
						}
					}
					
					// ADD TRANSITION FOR EACH ACTION
					
				}
				
        	]
        	actionCount.set(0, 0)
        ]
        
        // LOCATIONs AND TRANSITIONs FOR CONDITION
        
        
        // Transition when there is idle subsequences
        for (i : 1..maxIndex.get(0)) {
         	if (emptySS.get(i-1) == 0) {
        		x.set(0, xOffset)
            	y.set(0, i * yOffset)
            	// Reach the actual subSequence location
				transition.append(genTran(id.get(0), endSubSeqID.get(i-1), i, null, null, String.format("s%d?", i), null, true, 0, i * yOffset - 100, 0 ,0))
				id.set(0, id.get(0)+1)
				// Reach the idle location
				transition.append(genTran(id.get(0), i, endSubSeqID.get(i), null, null, null, String.format("%s_stat = true", sequence.agent.name.toLowerCase()), true, 0, i * yOffset, 0 ,0))
				id.set(0, id.get(0)+1)
        	} 
        	emptySS.set(i-1, 0)
        }
		
		// Add the transition to reach location for that ends the subsequence
		transition.append(genTran(id.get(0), endSubSeqID.get(maxIndex.get(0)), endID.get(0), null, null, String.format("e?"), null, true, 0, (maxIndex.get(0)+1) * yOffset - 100, 0 ,0))
		id.set(0, id.get(0)+1)
          
        builder.append(transition.toString)
        transition.delete(0, transition.length())
        builder.append("\t</template>\n")
    ]
	
	return builder.toString
}
	
	// Function that generate the string for the guard when the atomic predicate is position(agent, location)
	def apPositionGuard (Model model, String Agent, String Location){
			
		// Declarations
		val int[] locNum = #[0]
		val int[] hNum = #[0]
		val int[] rNum = #[0]
		val StringBuilder builder = new StringBuilder
		
		// Generate the array that contains the locations
	    model.pattern.location.forEach[ loc |
	    	locNum.set(0, locNum.get(0)+1)
	    ]
	    val String[] locations = newArrayOfSize(locNum.get(0))
	    locNum.set(0, 0)
		model.pattern.location.forEach[ loc |
	    	locations.set(locNum.get(0), loc.name.toString())
	    	locNum.set(0, locNum.get(0)+1)
	    ]
	    
	    // Generate the array that contains the humans
	    model.pattern.human.forEach[ hum |
	    	hNum.set(0, hNum.get(0)+1)
	    ]
	    val String[] humans = newArrayOfSize(hNum.get(0))
	    hNum.set(0, 0)
		model.pattern.human.forEach[ hum |
	    	humans.set(hNum.get(0), hum.name.toString())
	    	hNum.set(0, hNum.get(0)+1)
	    ]
	    
	    // Generate the array that contains the robots
	    model.pattern.robot.forEach[ rob |
	    	rNum.set(0, rNum.get(0)+1)
	    ]
	    val String[] robots = newArrayOfSize(rNum.get(0))
	    rNum.set(0, 0)
		model.pattern.robot.forEach[ rob |
	    	robots.set(rNum.get(0), rob.name.toString())
	    	rNum.set(0, rNum.get(0)+1)
	    ]
	    
	    val locIndex = locations.indexOf(Location)
	    // Check if the agent is a human
	    if (humans.contains(Agent)) {
	    	val hIndex = humans.indexOf(Agent)
	    	builder.append(String.format("humanPositionX["+ hIndex + "] &lt;= location_X["+ locIndex + "]+30 and
humanPositionX["+ hIndex + "] &gt;= location_X["+ locIndex + "]-30 and
humanPositionY["+ hIndex + "] &lt;= location_Y["+ locIndex + "]+30 and
humanPositionY["+ hIndex + "] &gt;= location_Y["+ locIndex + "]-30"))
	    }
	        // Check if the agent is a robot
	    if (robots.contains(Agent)) {
	    	val rIndex = robots.indexOf(Agent)
	    	builder.append(String.format("robPositionX["+ rIndex + "] &lt;= location_X["+ locIndex + "]+30 and
robPositionX["+ rIndex + "] &gt;= location_X["+ locIndex + "]-30 and
robPositionY["+ rIndex + "] &lt;= location_Y["+ locIndex + "]+30 and
robPositionY["+ rIndex + "] &gt;= location_Y["+ locIndex + "]-30"))
	    }
	    return builder.toString
	}

	
	// 
	
	// Function that generate a location in Uppaal
	def genLoc (int id, int x, int y, String name, String invariant, String Rate_of_exp, boolean urgent, boolean committed) {
		// genLoc(id, posX, posY, name, invariant, rate_of_exp, urgent, committed)
		// genLoc(0,  0,	0,	  null,	null,	   null,    	false,  false)
		
		val StringBuilder builder = new StringBuilder
		
		builder.append(String.format("\t\t<location id=\"id%d\" x=\"%d\" y=\"%d\">\n", id, x, y));
		
		if (name !== null)
			builder.append(String.format("\t\t\t<name x=\"%d\" y=\"%d\">%s</name>\n", x-17, y-34, name));
		if (invariant !== null)
			builder.append(String.format("\t\t\t<label kind=\"invariant\" x=\"%d\" y=\"%d\">%s</label>\n", x-10, y+17, invariant));
		if (Rate_of_exp !== null)
			builder.append(String.format("\t\t\t<label kind=\"exponentialrate\" x=\"%d\" y=\"%d\">%s</label>\n", x-10, y+34, Rate_of_exp));
		if (urgent !== false && committed !== true)
			builder.append(String.format("\t\t\t<urgent/>\n", id));
		if (committed !== false && urgent !== true)
			builder.append(String.format("\t\t\t<committed/>\n", id));
		
		builder.append("\t\t</location>\n")	
		
		return builder.toString
	}
	
	// Fanction that generate a transition in Uppaal
	def genTran (int id, int source, int target, String select, String guard, String sync, String update, boolean controllable, int xOffset, int yOffset, int nailX, int nailY) {
		val StringBuilder builder = new StringBuilder
		
		if (controllable == false) {
			builder.append(String.format("\t\t<transition id=\"id%d\" controllable=\"false\">\n", id));
		}else{
			builder.append(String.format("\t\t<transition id=\"id%d\">\n", id));
		}	
		builder.append(String.format("\t\t\t<source ref=\"id%d\"/>\n", source));
		builder.append(String.format("\t\t\t<target ref=\"id%d\"/>\n", target));
		
		if (xOffset == 500 && yOffset == 0) {  // Location for the pattern orchestrator
			if (select !== null)
				builder.append(String.format("\t\t\t<label kind=\"select\" x=\"%d\" y=\"%d\">%s</label>\n", 17+xOffset*source, -51+yOffset*source, select));
			if (guard !== null)
				builder.append(String.format("\t\t\t<label kind=\"guard\" x=\"%d\" y=\"%d\">%s</label>\n", 18+xOffset*source, -34+yOffset*source, guard));
			if (sync !== null)
				builder.append(String.format("\t\t\t<label kind=\"synchronisation\" x=\"%d\" y=\"%d\">%s</label>\n", 18+xOffset*source, -17+yOffset*source, sync));
			if (update !== null)
				builder.append(String.format("\t\t\t<label kind=\"assignment\" x=\"%d\" y=\"%d\">%s</label>\n", 18+xOffset*source, 0+yOffset*source, update));
			if (nailX !== 0 && nailY !== 0)
				builder.append(String.format("\t\t\t<nail x=\"%d\" y=\"%d\"/>\n", nailX, nailY));
		} else {  // Location for the sequence orchestrator
			if (select !== null)
				builder.append(String.format("\t\t\t<label kind=\"select\" x=\"%d\" y=\"%d\">%s</label>\n", 17+xOffset, -51+yOffset, select));
			if (guard !== null)
				builder.append(String.format("\t\t\t<label kind=\"guard\" x=\"%d\" y=\"%d\">%s</label>\n", 18+xOffset, -34+yOffset, guard));
			if (sync !== null)
				builder.append(String.format("\t\t\t<label kind=\"synchronisation\" x=\"%d\" y=\"%d\">%s</label>\n", 18+xOffset, -17+yOffset, sync));
			if (update !== null)
				builder.append(String.format("\t\t\t<label kind=\"assignment\" x=\"%d\" y=\"%d\">%s</label>\n", 18+xOffset, 0+yOffset, update));
			if (nailX !== 0 && nailY !== 0)
				builder.append(String.format("\t\t\t<nail x=\"%d\" y=\"%d\"/>\n", nailX, nailY));
		}
		builder.append("\t\t</transition>\n")	
		
		return builder.toString
	}
	
	
	// Function that generate verifier formulas
	def String verifier(Model model) {
		val simTime = 700
	    val pattern = model.pattern
	    val patternName = pattern.name.toString
	
	    // 1. Calcolo maxIndex efficiente (senza creare liste intermedie)
	    val allSS = model.sequence.flatMap[subSequence]
	    val maxIndex = allSS.map[Math.max(i, j)].max ?: 0
	
	    val queries = <String>newArrayList
	
	    /* --- Query per singola sequenza --- */
	    model.sequence.forEach[ seq |
	        val continuation = newIntArrayOfSize(maxIndex)
	        val emptySS = newIntArrayOfSize(maxIndex)
	        val agent = seq.agent.name.toLowerCase
	
	        seq.subSequence.forEach[ ss |
	            if (ss.j > ss.i) {
	                (ss.i + 1..ss.j).forEach[ k |
	                    continuation.set(k - 1, 1)
	                    emptySS.set(k - 1, 1)
	                ]
	            }
	            if (ss.actionC !== null && !ss.actionC.empty) {
	                emptySS.set(ss.i - 1, 1)
	            }
	        ]
	
	        val states = (1..maxIndex).map[ i |
	            val c = continuation.get(i - 1)
	            val e = emptySS.get(i - 1)
	            if (e == 0 && c == 0)      '''«patternName»_«agent»_def.S«i»_idle'''
	            else if (e == 1 && c == 0) '''«patternName»_«agent»_def.s«i»_end'''
	        ].filterNull.join(", ")
	
	        queries.add('''
	            <query>
	                <formula>simulate[&lt;=«simTime»]{«states», «patternName».end}</formula>
	            </query>
	        ''')
	    ]
	
	    /* --- Query Generali del Pattern --- */
	    val hCount = pattern.human.size
	    val rCount = pattern.robot.size
	    val resCount = pattern.resources.size
	
	    // Pattern termination (Pr[<=400])
	    queries.add('''
	        <query>
	            <formula>Pr[&lt;=«simTime»] (&lt;&gt; «patternName».end)</formula>
	            <comment>Check if the whole pattern ends within 400 time unit</comment>
	        </query>
	    ''')
	
	    // Posizioni Agenti
	    val posParts = (0..<hCount).map['''humanPositionX[«it»], humanPositionY[«it»]'''] +
	                   (0..<rCount).map['''robPositionX[«it»], robPositionY[«it»], batteryCharge[«it»]''']
	    
	    queries.add('''
	        <query>
	            <formula>simulate[&lt;=«simTime»]{«posParts.join(", ")», «patternName».end}</formula>
	            <comment>Check the position of all the agent in the pattern for 700 time unit</comment>
	        </query>
	    ''')
	
	    // Inventari Robot
	    val invs = (0..<rCount).flatMap[ r | 
	        (0..<resCount).map[ res | '''robot_inventory[«r»][«res»]'''] 
	    ].join(", ")
	
	    queries.add('''
	        <query>
	            <formula>simulate[&lt;=«simTime»]{«invs», «patternName».end*2}</formula>
	            <comment>Check quantity of each item for each robot</comment>
	        </query>
	    ''')
	
	    // Posizioni Location
	    val locs = pattern.location.map[name + "[0], " + name + "[1]"].join(", ")
	    
	    queries.add('''
	        <query>
	            <formula>simulate[&lt;=«simTime»]{«locs»}</formula>
	            <comment>Plot the position of each location in the pattern</comment>
	        </query>
	    ''')
	
	    // Robot Modes (simulate[<=700]{rm_1.idle, rf_1.idle, rp_1.idle, rd_1.idle})
	    val rmodes = (1..rCount).map[ i | 
	        '''rm_«i».idle, rf_«i».idle, rp_«i».idle, rd_«i».idle''' 
	    ].join(", ")
	    queries.add('''
	        <query>
	            <formula>simulate[&lt;=«simTime»]{«rmodes»}</formula>
	            <comment></comment>
	        </query>
	    ''')
	    
	    // Robot Modes (e.g. Pr[<=700](<> !rm_1.idle || !rf_1.idle || !rp_1.idle || !rd_1.idle))
	    val rmodes2 = (1..rCount).map[ i | 
	        '''!rm_«i».idle || !rf_«i».idle || !rp_«i».idle || !rd_«i».idle''' 
	    ].join(" || ")
	    queries.add('''
	        <query>
	            <formula>Pr[&lt;=«simTime»](&lt;&gt;«rmodes2»)</formula>
	            <comment></comment>
	        </query>
	    ''')
	    
	    // Robot Moves after the end? (e.g. Pr[<= 700] ([] (Coffee_Delivery.end imply (rm_1.idle && rf_1.idle && rp_1.idle && rd_1.idle))))
	    val rmodes3 = (1..rCount).map[ i | 
	        '''rm_«i».idle &amp;&amp; rf_«i».idle &amp;&amp; rp_«i».idle &amp;&amp; rd_«i».idle''' 
	    ].join(" &amp;&amp; ")
	    queries.add('''
	        <query>
	            <formula>Pr[&lt;=«simTime»]([] («patternName».end imply («rmodes3»)))</formula>
	            <comment></comment>
	        </query>
	    ''')
	    
	    
	    // Reachability of Targets for Robots (MoveTo check)
		// map of robot/human name to find the correct index
	    val robotList = pattern.robot.map[name]
	    val humanList = pattern.human.map[name]
	    
	    model.sequence.forEach[ seq |
	    	// Check if the agent in the seq. is a robot or a human
	    	val rIndex = robotList.indexOf(seq.agent.name)
	    	val hIndex = humanList.indexOf(seq.agent.name)
	    	// If rIndex != -1 => robot
	    	if (rIndex != -1) {
	    		// Avoid to generate twice the same property using a set (if an agent visit twice the same location)
	    		val targets = new java.util.HashSet<String>()
	    		seq.subSequence.forEach[ ss |
	    			ss.actionC.forEach[ act |
	    				// check if action moveTo
	    				if (act.actionType == "moveTo") {
	    					targets.add(act.target.name)
	    				}
	    			]
	    		]
	    		targets.forEach[ tName |
	    			queries.add('''
				        <query>
				            <formula>Pr[&lt;=«simTime»] (&lt;&gt; pow(robPositionX[«rIndex»] - «tName»[0], 2) + pow(robPositionY[«rIndex»] - «tName»[1], 2) &lt;= 900 )</formula>
				            <comment>Check if robot «seq.agent.name» reaches «tName» (within 30 units)</comment>
				        </query>
				    ''')
	    		]
	    	}
	    	// If hIndex != -1 => human
	    	if (hIndex != -1) {
	    		val targets = new java.util.HashSet<String>()
	    		seq.subSequence.forEach[ ss |
	    			ss.actionC.forEach[ act |
	    				if (act.actionType == "moveTo") {
	    					targets.add(act.target.name)
	    				}
	    			]
	    		]
	    		targets.forEach[ tName |
	    			queries.add('''
				        <query>
				            <formula>Pr[&lt;=«simTime»] (&lt;&gt; pow(humanPositionX[«rIndex»] - «tName»[0], 2) + pow(humanPositionY[«rIndex»] - «tName»[1], 2) &lt;= 900 )</formula>
				            <comment>Check if human «seq.agent.name» reaches «tName» (within 30 units)</comment>
				        </query>
				    ''')
	    		]
	    	}
	    ]
	   	
	   	// Valid Position / Collision Check
	    // Generate validity check for each HUMAN
	    model.pattern.human.forEach[ h, i |
	    	queries.add('''
		        <query>
		            <formula>Pr[&lt;=«simTime»] (&lt;&gt; !isValidPosition(humanPositionX[«i»], humanPositionY[«i»]))</formula>
		            <comment>Check if human «h.name» enters an invalid position (e.g. collision or obstacle)</comment>
		        </query>
		    ''')
	    ]
	    // Generate validity check for each ROBOT
	    model.pattern.robot.forEach[ r, i |
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

