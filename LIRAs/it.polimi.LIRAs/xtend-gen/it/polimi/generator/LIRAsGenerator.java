package it.polimi.generator;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import it.polimi.lIRAs.ActionC;
import it.polimi.lIRAs.AtomicPredicate;
import it.polimi.lIRAs.Condition;
import it.polimi.lIRAs.ConditionalStatement;
import it.polimi.lIRAs.Human;
import it.polimi.lIRAs.Location;
import it.polimi.lIRAs.Model;
import it.polimi.lIRAs.Pattern;
import it.polimi.lIRAs.Robot;
import it.polimi.lIRAs.Sequence;
import it.polimi.lIRAs.SubSequence;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.AbstractGenerator;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.generator.IGeneratorContext;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class LIRAsGenerator extends AbstractGenerator {
  public static class ActionConfig {
    public final String agentType;

    public final String actionName;

    public final String templateFileName;

    public final String syncChannels;

    public final String variables;

    public final String missionDefFormat;

    public final String missionDecPrefix;

    public final boolean requiresLocationsOrResources;

    public final boolean requiresResourcesOnly;

    public ActionConfig(final String agentType, final String actionName, final String templateFileName, final String syncChannels, final String variables, final String missionDefFormat, final String missionDecPrefix, final boolean requiresLocationsOrResources, final boolean requiresResourcesOnly) {
      this.agentType = agentType;
      this.actionName = actionName;
      this.templateFileName = templateFileName;
      this.syncChannels = syncChannels;
      this.variables = variables;
      this.missionDefFormat = missionDefFormat;
      this.missionDecPrefix = missionDecPrefix;
      this.requiresLocationsOrResources = requiresLocationsOrResources;
      this.requiresResourcesOnly = requiresResourcesOnly;
    }
  }

  public static class SeqActionConfig {
    public final String actionName;

    public final String agentType;

    public final String startSync;

    public final String stopSync;

    public final boolean hasInitialUpdate;

    public final boolean hasBusyLoop;

    public SeqActionConfig(final String actionName, final String agentType, final String startSync, final String stopSync, final boolean hasInitialUpdate, final boolean hasBusyLoop) {
      this.actionName = actionName;
      this.agentType = agentType;
      this.startSync = startSync;
      this.stopSync = stopSync;
      this.hasInitialUpdate = hasInitialUpdate;
      this.hasBusyLoop = hasBusyLoop;
    }
  }

  @Override
  public void doGenerate(final Resource resource, final IFileSystemAccess2 fsa, final IGeneratorContext context) {
    final long start = System.nanoTime();
    final Procedure1<Model> _function = (Model model) -> {
      final String xml = this.generateXML();
      final String filePath = String.format("%s.xml", model.getPattern().getName().toString());
      fsa.generateFile(filePath, xml);
      this.replace(filePath, "// **sub-sequences**", this.ssChan(model), fsa);
      this.replace(filePath, "// **agent_status**", this.sStatus(model), fsa);
      this.replace(filePath, "// **input_location**", this.iLocation(model), fsa);
      this.replace(filePath, "// **input_resource**", this.iResource(model), fsa);
      this.replace(filePath, "// **human_number**", this.hNumber(model), fsa);
      this.replace(filePath, "// **robot_number**", this.rNumber(model), fsa);
      this.replace(filePath, "// **resource_number**", this.iNumber(model), fsa);
      this.replace(filePath, "// **human_initialization**", this.hInit(model), fsa);
      this.replace(filePath, "// **robot_initialization**", this.rInit(model), fsa);
      this.injectActions(filePath, fsa, model);
      this.replace(filePath, "// **pattern_orchestrator**", this.pattOrch(model), fsa);
      this.replace(filePath, "// **sequence_orchestrator**", this.seqOrch(model), fsa);
      this.replace(filePath, "// **pattern_definition**", this.pattDef(model), fsa);
      this.replace(filePath, "// **pattern_dichiaration**", this.pattDic(model), fsa);
      this.replace(filePath, "// **verifier**", this.verifier(model), fsa);
    };
    IteratorExtensions.<Model>forEach(Iterators.<Model>filter(resource.getAllContents(), Model.class), _function);
    final long end = System.nanoTime();
    final long elapsedMs = ((end - start) / 1_000_000);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Generation completed in ");
    _builder.append(elapsedMs);
    _builder.append(" ms");
    InputOutput.<String>println(_builder.toString());
  }

  public String generateXML() {
    StringConcatenation _builder = new StringConcatenation();
    String _readTemplate = this.readTemplate();
    _builder.append(_readTemplate);
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  public String readTemplate() {
    try {
      final String filePath = "/Users/alberto_tagliaferro/Library/CloudStorage/OneDrive-PolitecnicodiMilano/Desktop/LIRAs_Generation/runtime-EclipseApplication/src-gen/Pre_Gen.xml";
      final List<String> lines = Files.readAllLines(Paths.get(filePath));
      return IterableExtensions.join(lines, "\n");
    } catch (final Throwable _t) {
      if (_t instanceof IOException) {
        final IOException e = (IOException)_t;
        e.printStackTrace();
        return "Error reading file";
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }

  public void replace(final String inputFileName, final String targetString, final String newString, final IFileSystemAccess2 fsa) {
    try {
      final CharSequence fileContent = fsa.readTextFile(inputFileName);
      if ((fileContent != null)) {
        final String updatedContent = fileContent.toString().replace(targetString, newString);
        fsa.generateFile(inputFileName, updatedContent);
      }
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception e = (Exception)_t;
        e.printStackTrace();
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }

  public String ssChan(final Model model) {
    Integer _elvis = null;
    final Function1<Sequence, EList<SubSequence>> _function = (Sequence it) -> {
      return it.getSubSequence();
    };
    final Function1<SubSequence, Integer> _function_1 = (SubSequence it) -> {
      return Integer.valueOf(Math.max(it.getI(), it.getJ()));
    };
    Integer _max = IterableExtensions.<Integer>max(IterableExtensions.<SubSequence, Integer>map(IterableExtensions.<Sequence, SubSequence>flatMap(model.getSequence(), _function), _function_1));
    if (_max != null) {
      _elvis = _max;
    } else {
      _elvis = Integer.valueOf(0);
    }
    final Integer maxSSnum = _elvis;
    if (((maxSSnum).intValue() == 0)) {
      return "urgent broadcast chan e;";
    }
    final Function1<Integer, String> _function_2 = (Integer it) -> {
      return ("s" + it);
    };
    final String channels = IterableExtensions.join(IterableExtensions.<Integer, String>map(new IntegerRange(1, (maxSSnum).intValue()), _function_2), ", ");
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("urgent broadcast chan ");
    _builder.append(channels);
    _builder.append(", e;");
    return _builder.toString();
  }

  public String sStatus(final Model model) {
    final Function1<Sequence, String> _function = (Sequence it) -> {
      String _lowerCase = it.getAgent().getName().toLowerCase();
      String _plus = ("bool " + _lowerCase);
      return (_plus + "_stat = true;");
    };
    String _join = IterableExtensions.join(ListExtensions.<Sequence, String>map(model.getSequence(), _function), "\n");
    return (_join + "\n");
  }

  public String iLocation(final Model model) {
    List<Location> _elvis = null;
    EList<Location> _location = model.getPattern().getLocation();
    if (_location != null) {
      _elvis = _location;
    } else {
      List<Location> _emptyList = CollectionLiterals.<Location>emptyList();
      _elvis = _emptyList;
    }
    final List<Location> locations = _elvis;
    List<it.polimi.lIRAs.Resource> _elvis_1 = null;
    EList<it.polimi.lIRAs.Resource> _resources = model.getPattern().getResources();
    if (_resources != null) {
      _elvis_1 = _resources;
    } else {
      List<it.polimi.lIRAs.Resource> _emptyList_1 = CollectionLiterals.<it.polimi.lIRAs.Resource>emptyList();
      _elvis_1 = _emptyList_1;
    }
    final List<it.polimi.lIRAs.Resource> resources = _elvis_1;
    int _size = locations.size();
    int _size_1 = resources.size();
    final int totalSize = (_size + _size_1);
    if ((totalSize == 0)) {
      return "// No locations or resources declared. Using dummy to prevent compilation errors.\n";
    }
    final Function1<Location, String> _function = (Location it) -> {
      String _name = it.getName();
      return (_name + "[0]");
    };
    List<String> _map = ListExtensions.<Location, String>map(locations, _function);
    final Function1<it.polimi.lIRAs.Resource, String> _function_1 = (it.polimi.lIRAs.Resource it) -> {
      String _name = it.getName();
      return (_name + "[0]");
    };
    List<String> _map_1 = ListExtensions.<it.polimi.lIRAs.Resource, String>map(resources, _function_1);
    final String stringX = IterableExtensions.join(Iterables.<String>concat(_map, _map_1), ", ");
    final Function1<Location, String> _function_2 = (Location it) -> {
      String _name = it.getName();
      return (_name + "[1]");
    };
    List<String> _map_2 = ListExtensions.<Location, String>map(locations, _function_2);
    final Function1<it.polimi.lIRAs.Resource, String> _function_3 = (it.polimi.lIRAs.Resource it) -> {
      String _name = it.getName();
      return (_name + "[1]");
    };
    List<String> _map_3 = ListExtensions.<it.polimi.lIRAs.Resource, String>map(resources, _function_3);
    final String stringY = IterableExtensions.join(Iterables.<String>concat(_map_2, _map_3), ", ");
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("double location_X[");
    _builder.append(totalSize);
    _builder.append("] = {");
    _builder.append(stringX);
    _builder.append("};");
    _builder.newLineIfNotEmpty();
    _builder.append("double location_Y[");
    _builder.append(totalSize);
    _builder.append("] = {");
    _builder.append(stringY);
    _builder.append("};");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  public String iResource(final Model model) {
    final EList<it.polimi.lIRAs.Resource> resources = model.getPattern().getResources();
    boolean _isNullOrEmpty = IterableExtensions.isNullOrEmpty(resources);
    if (_isNullOrEmpty) {
      return "// No resources in pattern\n";
    }
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("double resources_X[");
    int _size = resources.size();
    _builder.append(_size);
    _builder.append("] = {");
    final Function1<it.polimi.lIRAs.Resource, String> _function = (it.polimi.lIRAs.Resource it) -> {
      String _name = it.getName();
      return (_name + "[0]");
    };
    String _join = IterableExtensions.join(ListExtensions.<it.polimi.lIRAs.Resource, String>map(resources, _function), ", ");
    _builder.append(_join);
    _builder.append("};");
    _builder.newLineIfNotEmpty();
    _builder.append("double resources_Y[");
    int _size_1 = resources.size();
    _builder.append(_size_1);
    _builder.append("] = {");
    final Function1<it.polimi.lIRAs.Resource, String> _function_1 = (it.polimi.lIRAs.Resource it) -> {
      String _name = it.getName();
      return (_name + "[1]");
    };
    String _join_1 = IterableExtensions.join(ListExtensions.<it.polimi.lIRAs.Resource, String>map(resources, _function_1), ", ");
    _builder.append(_join_1);
    _builder.append("};");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  public String hNumber(final Model model) {
    String _xblockexpression = null;
    {
      final int hNum = model.getPattern().getHuman().size();
      String _xifexpression = null;
      if ((hNum > 0)) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("const int N_H = ");
        _builder.append(hNum);
        _builder.append("; // Number of humans in the pattern");
        _xifexpression = _builder.toString();
      } else {
        _xifexpression = "// There are no Humans in the pattern Input";
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }

  public String rNumber(final Model model) {
    String _xblockexpression = null;
    {
      final int rNum = model.getPattern().getRobot().size();
      String _xifexpression = null;
      if ((rNum > 0)) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("const int N_R = ");
        _builder.append(rNum);
        _builder.append("; // Number of robots in the pattern");
        _xifexpression = _builder.toString();
      } else {
        _xifexpression = "// There are no Robots in the pattern Input";
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }

  public String iNumber(final Model model) {
    String _xblockexpression = null;
    {
      final int iNum = model.getPattern().getResources().size();
      String _xifexpression = null;
      if ((iNum > 0)) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("const int N_ITEMS = ");
        _builder.append(iNum);
        _builder.append("; // Number of resources in the pattern");
        _xifexpression = _builder.toString();
      } else {
        _xifexpression = "// There are no Resources in the pattern Input";
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }

  public String hInit(final Model model) {
    final int hNum = model.getPattern().getHuman().size();
    if ((hNum == 0)) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("// No humans declared: declare dummy arrays to prevent compilation errors");
      _builder.newLine();
      _builder.append("double humanPositionX[1] = {0.0};");
      _builder.newLine();
      _builder.append("double humanPositionY[1] = {0.0};");
      _builder.newLine();
      return _builder.toString();
    }
    final Function1<Integer, String> _function = (Integer it) -> {
      return "0.0";
    };
    final String zeros = IterableExtensions.join(IterableExtensions.<Integer, String>map(new IntegerRange(1, hNum), _function), ", ");
    final Function1<Integer, String> _function_1 = (Integer it) -> {
      return it.toString();
    };
    final String nodeIndices = IterableExtensions.join(IterableExtensions.<Integer, String>map(new ExclusiveRange(0, hNum, true), _function_1), ", ");
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("// Human initialization");
    _builder_1.newLine();
    _builder_1.append("double humanFatigue[N_H] = {");
    _builder_1.append(zeros);
    _builder_1.append("};");
    _builder_1.newLineIfNotEmpty();
    _builder_1.append("// The humans will start from the first N_H intersections");
    _builder_1.newLine();
    _builder_1.append("const int H_start_nodes[N_H] = {");
    _builder_1.append(nodeIndices);
    _builder_1.append("};");
    _builder_1.newLineIfNotEmpty();
    _builder_1.append("double humanPositionX[N_H] = {");
    _builder_1.append(zeros);
    _builder_1.append("};");
    _builder_1.newLineIfNotEmpty();
    _builder_1.append("double humanPositionY[N_H] = {");
    _builder_1.append(zeros);
    _builder_1.append("};");
    _builder_1.newLineIfNotEmpty();
    return _builder_1.toString();
  }

  public String rInit(final Model model) {
    final int rNum = model.getPattern().getRobot().size();
    if ((rNum == 0)) {
      return "// No input Robot has been declared\n";
    }
    final Function1<Integer, String> _function = (Integer it) -> {
      return "100.0";
    };
    final String charges = IterableExtensions.join(IterableExtensions.<Integer, String>map(new IntegerRange(1, rNum), _function), ", ");
    final Function1<Integer, String> _function_1 = (Integer it) -> {
      return "0.0";
    };
    final String zeros = IterableExtensions.join(IterableExtensions.<Integer, String>map(new IntegerRange(1, rNum), _function_1), ", ");
    final Function1<Integer, String> _function_2 = (Integer it) -> {
      return it.toString();
    };
    final String nodeIndices = IterableExtensions.join(IterableExtensions.<Integer, String>map(new ExclusiveRange(0, rNum, true), _function_2), ", ");
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("// Robot initialization");
    _builder.newLine();
    _builder.append("double batteryCharge[N_R] = {");
    _builder.append(charges);
    _builder.append("};");
    _builder.newLineIfNotEmpty();
    _builder.append("// The robots will start from the first N_R intersections");
    _builder.newLine();
    _builder.append("const int R_start_nodes[N_R] = {");
    _builder.append(nodeIndices);
    _builder.append("};");
    _builder.newLineIfNotEmpty();
    _builder.append("double robPositionX[N_R] = {");
    _builder.append(zeros);
    _builder.append("};");
    _builder.newLineIfNotEmpty();
    _builder.append("double robPositionY[N_R] = {");
    _builder.append(zeros);
    _builder.append("};");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  public List<LIRAsGenerator.ActionConfig> getActionRegistry() {
    LIRAsGenerator.ActionConfig _actionConfig = new LIRAsGenerator.ActionConfig("HUMAN", "idle", "Human_idle.xml", 
      "urgent broadcast chan obey_h_moveTo[N_H];", 
      "", 
      "hi_%d = Human_idle(%d);", "hi_", false, false);
    LIRAsGenerator.ActionConfig _actionConfig_1 = new LIRAsGenerator.ActionConfig("HUMAN", "moveTo", "Human_moveTo.xml", 
      "urgent broadcast chan start_h_moveTo[N_H], stop_h_moveTo[N_H];\nbroadcast chan hm_fail;", 
      "int human_loc_target[N_H];", 
      "hm_%d = Human_moveTo(%d, 26.0);", "hm_", true, false);
    LIRAsGenerator.ActionConfig _actionConfig_2 = new LIRAsGenerator.ActionConfig("ROBOT", "moveTo", "Robot_moveTo.xml", 
      "urgent broadcast chan start_r_moveTo[N_R], stop_r_moveTo[N_R];\nbroadcast chan rm_still;", 
      "int robot_loc_target[N_R];", 
      "rm_%d = Robot_moveTo(%d, 26.0, 26.0);", "rm_", true, false);
    LIRAsGenerator.ActionConfig _actionConfig_3 = new LIRAsGenerator.ActionConfig("ROBOT", "follow", "Robot_follow.xml", 
      "urgent broadcast chan start_r_follow[N_R], stop_r_follow[N_R];\nbroadcast chan rf_still;", 
      "// Who I have to follow (agent id)?\nint human_to_be_followed[N_R];\nint robot_to_be_followed[N_R];\nint f_loc_target[N_R];", 
      "rf_%d = Robot_follow(%d, 26.0, 26.0);", "rf_", false, false);
    LIRAsGenerator.ActionConfig _actionConfig_4 = new LIRAsGenerator.ActionConfig("ROBOT", "pick", "Robot_pick.xml", 
      "urgent broadcast chan start_r_pick[N_R], stop_r_pick[N_R];", 
      "int robot_res_to_pick[N_R];\nint robot_inventory[N_R][N_ITEMS];\nint old_inventory[N_R][N_ITEMS];", 
      "rp_%d = Robot_pick(%d);", "rp_", false, true);
    LIRAsGenerator.ActionConfig _actionConfig_5 = new LIRAsGenerator.ActionConfig("ROBOT", "deposit", "Robot_deposit.xml", 
      "urgent broadcast chan start_r_deposit[N_R], stop_r_deposit[N_R];", 
      "int robot_res_to_deposit[N_R];", 
      "rd_%d = Robot_deposit(%d);", "rd_", false, true);
    return Collections.<LIRAsGenerator.ActionConfig>unmodifiableList(CollectionLiterals.<LIRAsGenerator.ActionConfig>newArrayList(_actionConfig, _actionConfig_1, _actionConfig_2, _actionConfig_3, _actionConfig_4, _actionConfig_5));
  }

  public void injectActions(final String filePath, final IFileSystemAccess2 fsa, final Model model) {
    final int hNum = model.getPattern().getHuman().size();
    final int rNum = model.getPattern().getRobot().size();
    final int lNum = model.getPattern().getLocation().size();
    final int resNum = model.getPattern().getResources().size();
    final boolean hasLocationsOrResources = ((lNum > 0) || (resNum > 0));
    final boolean hasResources = (resNum > 0);
    final StringBuilder syncBuilder = new StringBuilder();
    final StringBuilder varBuilder = new StringBuilder();
    final StringBuilder templateBuilder = new StringBuilder();
    final StringBuilder missDefBuilder = new StringBuilder();
    final StringBuilder missDecBuilder = new StringBuilder();
    List<LIRAsGenerator.ActionConfig> _actionRegistry = this.getActionRegistry();
    for (final LIRAsGenerator.ActionConfig config : _actionRegistry) {
      {
        int _xifexpression = (int) 0;
        boolean _equals = Objects.equals(config.agentType, "HUMAN");
        if (_equals) {
          _xifexpression = hNum;
        } else {
          _xifexpression = rNum;
        }
        final int agentCount = _xifexpression;
        final boolean agentConditionMet = (agentCount > 0);
        final boolean locResConditionMet = ((!config.requiresLocationsOrResources) || hasLocationsOrResources);
        final boolean resConditionMet = ((!config.requiresResourcesOnly) || hasResources);
        if (((agentConditionMet && locResConditionMet) && resConditionMet)) {
          boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(config.syncChannels);
          boolean _not = (!_isNullOrEmpty);
          if (_not) {
            syncBuilder.append(config.syncChannels).append("\n");
          }
          boolean _isNullOrEmpty_1 = StringExtensions.isNullOrEmpty(config.variables);
          boolean _not_1 = (!_isNullOrEmpty_1);
          if (_not_1) {
            varBuilder.append(config.variables).append("\n");
          }
          templateBuilder.append(this.readActionTemplate(config.templateFileName)).append("\n");
          final Function1<Integer, String> _function = (Integer it) -> {
            return String.format(config.missionDefFormat, it, it);
          };
          final String defs = IterableExtensions.join(IterableExtensions.<Integer, String>map(new IntegerRange(1, agentCount), _function), "\n");
          missDefBuilder.append(defs).append("\n");
          final Function1<Integer, String> _function_1 = (Integer it) -> {
            return (config.missionDecPrefix + it);
          };
          final String decs = IterableExtensions.join(IterableExtensions.<Integer, String>map(new IntegerRange(1, agentCount), _function_1), ",\n");
          missDecBuilder.append(decs).append(",\n");
        }
      }
    }
    this.replace(filePath, "// **actions_sync_channels**", syncBuilder.toString(), fsa);
    this.replace(filePath, "// **actions_variables**", varBuilder.toString(), fsa);
    this.replace(filePath, "// **actions_templates**", templateBuilder.toString(), fsa);
    this.replace(filePath, "// **actions_mission_definitions**", missDefBuilder.toString(), fsa);
    this.replace(filePath, "// **actions_mission_declarations**", missDecBuilder.toString(), fsa);
  }

  public String readActionTemplate(final String fileName) {
    final String basePath = "/Users/alberto_tagliaferro/Library/CloudStorage/OneDrive-PolitecnicodiMilano/Desktop/LIRAs_Generation/runtime-EclipseApplication/src-gen/";
    final String fullPath = (basePath + fileName);
    try {
      return IterableExtensions.join(Files.readAllLines(Paths.get(fullPath)), "\n");
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception e = (Exception)_t;
        e.printStackTrace();
        return "";
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }

  public String pattDef(final Model model) {
    final String patternName = model.getPattern().getName().toString();
    final StringBuilder builder = new StringBuilder();
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(patternName);
    _builder.append(" = ");
    _builder.append(patternName);
    _builder.append("_orchestrator(1);");
    builder.append(_builder).append("\n");
    final Function1<Human, String> _function = (Human it) -> {
      return it.getName();
    };
    final Set<String> humanNames = IterableExtensions.<String>toSet(ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function));
    final Function1<Robot, String> _function_1 = (Robot it) -> {
      return it.getName();
    };
    final Set<String> robotNames = IterableExtensions.<String>toSet(ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_1));
    int hIndex = 1;
    int rIndex = 1;
    EList<Sequence> _sequence = model.getSequence();
    for (final Sequence sequence : _sequence) {
      {
        final String agentName = sequence.getAgent().getName();
        final String agentLower = agentName.toLowerCase();
        boolean _contains = humanNames.contains(agentName);
        if (_contains) {
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append(patternName);
          _builder_1.append("_");
          _builder_1.append(agentLower);
          _builder_1.append("_def = ");
          _builder_1.append(patternName);
          _builder_1.append("_");
          _builder_1.append(agentLower);
          _builder_1.append("(1, ");
          _builder_1.append(hIndex);
          _builder_1.append(");");
          builder.append(_builder_1).append("\n");
          hIndex++;
        } else {
          boolean _contains_1 = robotNames.contains(agentName);
          if (_contains_1) {
            StringConcatenation _builder_2 = new StringConcatenation();
            _builder_2.append(patternName);
            _builder_2.append("_");
            _builder_2.append(agentLower);
            _builder_2.append("_def = ");
            _builder_2.append(patternName);
            _builder_2.append("_");
            _builder_2.append(agentLower);
            _builder_2.append("(1, ");
            _builder_2.append(rIndex);
            _builder_2.append(");");
            builder.append(_builder_2).append("\n");
            rIndex++;
          }
        }
      }
    }
    return builder.toString();
  }

  public String pattDic(final Model model) {
    final String patternName = model.getPattern().getName().toString();
    final String mainOrch = patternName;
    final Function1<Sequence, String> _function = (Sequence it) -> {
      String _xblockexpression = null;
      {
        final String agentLower = it.getAgent().getName().toLowerCase();
        StringConcatenation _builder = new StringConcatenation();
        _builder.append(patternName);
        _builder.append("_");
        _builder.append(agentLower);
        _builder.append("_def");
        _xblockexpression = _builder.toString();
      }
      return _xblockexpression;
    };
    final List<String> seqDefs = ListExtensions.<Sequence, String>map(model.getSequence(), _function);
    final ArrayList<String> allDeclarations = CollectionLiterals.<String>newArrayList(mainOrch);
    allDeclarations.addAll(seqDefs);
    return IterableExtensions.join(allDeclarations, ",\n");
  }

  public String pattOrch(final Model model) {
    final String patternName = model.getPattern().getName().toString();
    final StringBuilder builder = new StringBuilder();
    final int xOffset = 500;
    final int yOffset = 0;
    String locName = null;
    String invariant = null;
    String rate_of_exp = null;
    boolean urgent = false;
    boolean committed = false;
    int source = 0;
    int target = 0;
    String select = null;
    String sync = null;
    boolean controllable = true;
    int nailX = 0;
    int nailY = 0;
    builder.append("\t<template>\n");
    builder.append(String.format("\t\t<name>%s_orchestrator</name>\n", patternName));
    builder.append("\t\t<parameter>int p</parameter>\n");
    builder.append("\t\t<declaration>// Place local declarations here.\nclock end_c;\n\t\t</declaration>\n");
    Integer _elvis = null;
    final Function1<Sequence, EList<SubSequence>> _function = (Sequence it) -> {
      return it.getSubSequence();
    };
    final Function1<SubSequence, Integer> _function_1 = (SubSequence it) -> {
      return Integer.valueOf(Math.max(it.getI(), it.getJ()));
    };
    Integer _max = IterableExtensions.<Integer>max(IterableExtensions.<SubSequence, Integer>map(IterableExtensions.<Sequence, SubSequence>flatMap(model.getSequence(), _function), _function_1));
    if (_max != null) {
      _elvis = _max;
    } else {
      _elvis = Integer.valueOf(0);
    }
    final Integer maxIndexHelper = _elvis;
    int locId = 0;
    locName = "Init";
    invariant = null;
    rate_of_exp = null;
    urgent = false;
    committed = false;
    builder.append(this.genLoc(locId, (0 * xOffset), yOffset, locName, invariant, rate_of_exp, urgent, committed));
    locId++;
    IntegerRange _upTo = new IntegerRange(1, ((maxIndexHelper).intValue() + 2));
    for (final Integer i : _upTo) {
      {
        String _xifexpression = null;
        boolean _lessEqualsThan = (i.compareTo(maxIndexHelper) <= 0);
        if (_lessEqualsThan) {
          _xifexpression = String.format("sub_seq_s%d", i);
        } else {
          String _xifexpression_1 = null;
          if (((i).intValue() == ((maxIndexHelper).intValue() + 1))) {
            _xifexpression_1 = "pre_end";
          } else {
            _xifexpression_1 = "end";
          }
          _xifexpression = _xifexpression_1;
        }
        locName = _xifexpression;
        String _xifexpression_2 = null;
        if (((i).intValue() == ((maxIndexHelper).intValue() + 1))) {
          _xifexpression_2 = "end_c &lt;= 2";
        } else {
          _xifexpression_2 = null;
        }
        invariant = _xifexpression_2;
        rate_of_exp = null;
        urgent = false;
        committed = false;
        builder.append(this.genLoc(locId, ((i).intValue() * xOffset), yOffset, locName, invariant, rate_of_exp, urgent, committed));
        locId++;
      }
    }
    builder.append("\t\t<init ref=\"id0\"/>\n");
    IntegerRange _upTo_1 = new IntegerRange(0, ((maxIndexHelper).intValue() - 1));
    for (final Integer i_1 : _upTo_1) {
      {
        final ArrayList<String> agentsToSync = new ArrayList<String>();
        EList<Sequence> _sequence = model.getSequence();
        for (final Sequence sequence : _sequence) {
          {
            boolean hasContinuation = false;
            EList<SubSequence> _subSequence = sequence.getSubSequence();
            for (final SubSequence subSeq : _subSequence) {
              int _j = subSeq.getJ();
              int _i = subSeq.getI();
              int _minus = (_j - _i);
              boolean _greaterThan = (_minus > 0);
              if (_greaterThan) {
                if ((((i_1).intValue() >= subSeq.getI()) && ((i_1).intValue() < subSeq.getJ()))) {
                  hasContinuation = true;
                }
              }
            }
            if ((!hasContinuation)) {
              agentsToSync.add(sequence.getAgent().getName().toLowerCase());
            }
          }
        }
        final ArrayList<String> guardsList = new ArrayList<String>();
        for (final String agent : agentsToSync) {
          guardsList.add((agent + "_stat==true"));
        }
        if (((i_1).intValue() == 0)) {
          guardsList.add("p==currP");
        }
        final String guardStr = IterableExtensions.join(guardsList, " and ");
        final Function1<String, String> _function_2 = (String it) -> {
          return (it + "_stat=false");
        };
        final String updateStr = IterableExtensions.join(ListExtensions.<String, String>map(agentsToSync, _function_2), ", ");
        source = (i_1).intValue();
        target = ((i_1).intValue() + 1);
        select = null;
        sync = String.format("s%d!", Integer.valueOf(((i_1).intValue() + 1)));
        controllable = true;
        nailX = 0;
        nailY = 0;
        builder.append(this.genTran(locId, source, target, select, guardStr, sync, updateStr, controllable, xOffset, yOffset, nailX, nailY));
        locId++;
        if (((i_1).intValue() == ((maxIndexHelper).intValue() - 1))) {
          final Function1<Sequence, String> _function_3 = (Sequence it) -> {
            String _lowerCase = it.getAgent().getName().toLowerCase();
            return (_lowerCase + "_stat==true");
          };
          final String finalGuards = IterableExtensions.join(ListExtensions.<Sequence, String>map(model.getSequence(), _function_3), " and ");
          final String finalUpdate = "end_c=0";
          source = ((i_1).intValue() + 1);
          target = ((i_1).intValue() + 2);
          select = null;
          sync = "e!";
          controllable = true;
          nailX = 0;
          nailY = 0;
          builder.append(this.genTran(locId, source, target, select, finalGuards, sync, finalUpdate, controllable, xOffset, yOffset, nailX, nailY));
          locId++;
        }
      }
    }
    source = ((maxIndexHelper).intValue() + 1);
    target = ((maxIndexHelper).intValue() + 2);
    select = null;
    sync = null;
    controllable = true;
    nailX = 0;
    nailY = 0;
    final String endGuard = "end_c &gt;= 2";
    final String endUpdate = "currP=currP+1";
    builder.append(this.genTran(locId, source, target, select, endGuard, sync, endUpdate, controllable, xOffset, yOffset, nailX, nailY));
    locId++;
    builder.append("\t</template>\n");
    return builder.toString();
  }

  public List<LIRAsGenerator.SeqActionConfig> getSeqActionRegistry() {
    LIRAsGenerator.SeqActionConfig _seqActionConfig = new LIRAsGenerator.SeqActionConfig("moveTo", "human", "start_h_moveTo[H-1]!", "stop_h_moveTo[H-1]!", true, true);
    LIRAsGenerator.SeqActionConfig _seqActionConfig_1 = new LIRAsGenerator.SeqActionConfig("moveTo", "rob", "start_r_moveTo[R-1]!", "stop_r_moveTo[R-1]!", true, false);
    LIRAsGenerator.SeqActionConfig _seqActionConfig_2 = new LIRAsGenerator.SeqActionConfig("follow", "rob", "start_r_follow[R-1]!", "stop_r_follow[R-1]!", true, false);
    LIRAsGenerator.SeqActionConfig _seqActionConfig_3 = new LIRAsGenerator.SeqActionConfig("pick", "rob", "start_r_pick[R-1]!", "stop_r_pick[R-1]!", true, false);
    LIRAsGenerator.SeqActionConfig _seqActionConfig_4 = new LIRAsGenerator.SeqActionConfig("deposit", "rob", "start_r_deposit[R-1]!", "stop_r_deposit[R-1]!", true, false);
    return Collections.<LIRAsGenerator.SeqActionConfig>unmodifiableList(CollectionLiterals.<LIRAsGenerator.SeqActionConfig>newArrayList(_seqActionConfig, _seqActionConfig_1, _seqActionConfig_2, _seqActionConfig_3, _seqActionConfig_4));
  }

  public LIRAsGenerator.SeqActionConfig getSeqActionConfig(final String actionName, final String agentPrefix) {
    final Function1<LIRAsGenerator.SeqActionConfig, Boolean> _function = (LIRAsGenerator.SeqActionConfig it) -> {
      return Boolean.valueOf((Objects.equals(actionName, it.actionName) && Objects.equals(agentPrefix, it.agentType)));
    };
    return IterableExtensions.<LIRAsGenerator.SeqActionConfig>findFirst(this.getSeqActionRegistry(), _function);
  }

  public ActionC getNextAction(final EList<ActionC> actions, final ActionC currentAction) {
    final int index = actions.indexOf(currentAction);
    if (((index != (-1)) && (index < (actions.size() - 1)))) {
      return actions.get((index + 1));
    }
    return null;
  }

  public String seqOrch(final Model model) {
    final StringBuilder transition = new StringBuilder();
    final StringBuilder builder = new StringBuilder();
    final StringBuilder guard = new StringBuilder();
    final StringBuilder update = new StringBuilder();
    String locName = null;
    String invariant = null;
    String rate_of_exp = null;
    boolean urgent = false;
    boolean committed = false;
    int source = 0;
    int target = 0;
    String select = null;
    String sync = null;
    boolean controllable = true;
    int nailX = 0;
    int nailY = 0;
    final int xOffset = 200;
    final int yOffset = 350;
    int actionCount = 0;
    Integer _elvis = null;
    final Function1<Sequence, EList<SubSequence>> _function = (Sequence it) -> {
      return it.getSubSequence();
    };
    final Function1<SubSequence, Integer> _function_1 = (SubSequence it) -> {
      return Integer.valueOf(Math.max(it.getI(), it.getJ()));
    };
    Integer _max = IterableExtensions.<Integer>max(IterableExtensions.<SubSequence, Integer>map(IterableExtensions.<Sequence, SubSequence>flatMap(model.getSequence(), _function), _function_1));
    if (_max != null) {
      _elvis = _max;
    } else {
      _elvis = Integer.valueOf(0);
    }
    final Integer maxIndex = _elvis;
    int AgentID = 0;
    EList<Sequence> _sequence = model.getSequence();
    for (final Sequence sequence : _sequence) {
      {
        AgentID++;
        int id = 0;
        int x = 0;
        int y = 0;
        int actionID = 1;
        int endID = 0;
        final int[] continuation = new int[(maxIndex).intValue()];
        final int[] emptySS = new int[(maxIndex).intValue()];
        final int[] firstActionID = new int[((maxIndex).intValue() + 1)];
        final int[] lastActionID = new int[((maxIndex).intValue() + 1)];
        final int[] endSubSeqID = new int[((maxIndex).intValue() + 1)];
        EList<SubSequence> _subSequence = sequence.getSubSequence();
        for (final SubSequence subSequence : _subSequence) {
          int _j = subSequence.getJ();
          int _i = subSequence.getI();
          int _minus = (_j - _i);
          boolean _greaterThan = (_minus > 0);
          if (_greaterThan) {
            int _i_1 = subSequence.getI();
            int _plus = (_i_1 + 1);
            int _j_1 = subSequence.getJ();
            IntegerRange _upTo = new IntegerRange(_plus, _j_1);
            for (final Integer j : _upTo) {
              continuation[((j).intValue() - 1)] = 1;
            }
          }
        }
        final String agentName = sequence.getAgent().getName().toString();
        final Function1<Human, String> _function_2 = (Human it) -> {
          return it.getName().toString();
        };
        final boolean isHuman = IterableExtensions.<String>toList(ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_2)).contains(agentName);
        final Function1<Robot, String> _function_3 = (Robot it) -> {
          return it.getName().toString();
        };
        final boolean isRobot = IterableExtensions.<String>toList(ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_3)).contains(agentName);
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("<template>");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("<name>");
        String _name = model.getPattern().getName();
        _builder.append(_name, "\t");
        _builder.append("_");
        String _lowerCase = agentName.toLowerCase();
        _builder.append(_lowerCase, "\t");
        _builder.append("</name>");
        _builder.newLineIfNotEmpty();
        {
          if (isHuman) {
            _builder.append("\t");
            _builder.append("<parameter>int p, int H</parameter>");
            _builder.newLine();
          }
        }
        {
          if (isRobot) {
            _builder.append("\t");
            _builder.append("<parameter>int p, int R</parameter>");
            _builder.newLine();
          }
        }
        _builder.append("\t");
        _builder.append("<declaration>// Place local declarations here.");
        _builder.newLine();
        _builder.append("clock t;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("</declaration>");
        _builder.newLine();
        builder.append(_builder);
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("init_");
        String _lowerCase_1 = agentName.toLowerCase();
        _builder_1.append(_lowerCase_1);
        locName = _builder_1.toString();
        invariant = null;
        rate_of_exp = null;
        urgent = false;
        committed = false;
        builder.append(this.genLoc(id, x, y, locName, invariant, rate_of_exp, urgent, committed));
        id++;
        IntegerRange _upTo_1 = new IntegerRange(1, (maxIndex).intValue());
        for (final Integer i : _upTo_1) {
          {
            int _y = y;
            y = (_y + yOffset);
            x = 0;
            int _get = continuation[((i).intValue() - 1)];
            boolean _equals = (_get == 0);
            if (_equals) {
              StringConcatenation _builder_2 = new StringConcatenation();
              _builder_2.append("s");
              _builder_2.append(i);
              _builder_2.append("_start");
              locName = _builder_2.toString();
              invariant = null;
              rate_of_exp = null;
              urgent = false;
              committed = true;
              builder.append(this.genLoc(id, x, y, locName, invariant, rate_of_exp, urgent, committed));
            }
            id++;
            continuation[((i).intValue() - 1)] = 0;
            boolean _equals_1 = Objects.equals(i, maxIndex);
            if (_equals_1) {
              int _y_1 = y;
              y = (_y_1 + yOffset);
              x = 0;
              StringConcatenation _builder_3 = new StringConcatenation();
              _builder_3.append("end_");
              String _lowerCase_2 = agentName.toLowerCase();
              _builder_3.append(_lowerCase_2);
              locName = _builder_3.toString();
              invariant = null;
              rate_of_exp = null;
              urgent = false;
              committed = false;
              builder.append(this.genLoc(id, x, y, locName, invariant, rate_of_exp, urgent, committed));
              endID = id;
              id++;
            }
          }
        }
        y = 0;
        EList<SubSequence> _subSequence_1 = sequence.getSubSequence();
        for (final SubSequence subSequence_1 : _subSequence_1) {
          {
            int _j_2 = subSequence_1.getJ();
            boolean _notEquals = (_j_2 != 0);
            if (_notEquals) {
              int _i_2 = subSequence_1.getI();
              int _plus_1 = (_i_2 + 1);
              int _j_3 = subSequence_1.getJ();
              IntegerRange _upTo_2 = new IntegerRange(_plus_1, _j_3);
              for (final Integer i_1 : _upTo_2) {
                emptySS[((i_1).intValue() - 1)] = 1;
              }
            }
            x = 0;
            int _i_3 = subSequence_1.getI();
            int _multiply = (_i_3 * yOffset);
            y = _multiply;
            EList<ActionC> _actionC = subSequence_1.getActionC();
            for (final ActionC actionC : _actionC) {
              {
                int _i_4 = subSequence_1.getI();
                int _minus_1 = (_i_4 - 1);
                emptySS[_minus_1] = 1;
                final String aType = actionC.getActionType().toString();
                int _x = x;
                x = (_x + xOffset);
                StringConcatenation _builder_2 = new StringConcatenation();
                _builder_2.append("A");
                _builder_2.append(AgentID);
                _builder_2.append("_a");
                _builder_2.append(actionID);
                _builder_2.append("_");
                _builder_2.append(aType);
                _builder_2.append("_busy");
                locName = _builder_2.toString();
                String _xifexpression = null;
                if (isHuman) {
                  _xifexpression = "t&lt;=5";
                } else {
                  _xifexpression = null;
                }
                invariant = _xifexpression;
                rate_of_exp = null;
                urgent = false;
                committed = false;
                builder.append(this.genLoc(id, x, y, locName, invariant, rate_of_exp, urgent, committed));
                if ((x == xOffset)) {
                  firstActionID[subSequence_1.getI()] = id;
                }
                id++;
                int _x_1 = x;
                x = (_x_1 + xOffset);
                StringConcatenation _builder_3 = new StringConcatenation();
                _builder_3.append("A");
                _builder_3.append(AgentID);
                _builder_3.append("_a");
                _builder_3.append(actionID);
                _builder_3.append("_");
                _builder_3.append(aType);
                _builder_3.append("_complete");
                locName = _builder_3.toString();
                invariant = null;
                rate_of_exp = null;
                urgent = false;
                committed = true;
                builder.append(this.genLoc(id, x, y, locName, invariant, rate_of_exp, urgent, committed));
                lastActionID[subSequence_1.getI()] = id;
                id++;
                actionID++;
              }
            }
            actionCount = 0;
            EList<ActionC> _actionC_1 = subSequence_1.getActionC();
            for (final ActionC actionC_1 : _actionC_1) {
              {
                actionCount++;
                boolean _isEmpty = actionC_1.getConditionalStatement().isEmpty();
                boolean _not = (!_isEmpty);
                if (_not) {
                  EList<ConditionalStatement> _conditionalStatement = actionC_1.getConditionalStatement();
                  for (final ConditionalStatement cs : _conditionalStatement) {
                    {
                      EList<Condition> _condition = cs.getCondition();
                      for (final Condition c : _condition) {
                        {
                          final AtomicPredicate pred0 = c.getAtomicPredicate().get(0);
                          String _ap = pred0.getAp();
                          boolean _equals = Objects.equals(_ap, "position");
                          if (_equals) {
                            final String baseGuard = this.apPositionGuard(model, 
                              pred0.getAgent().getName().toString(), 
                              pred0.getLocation().getName().toString());
                            boolean _isNegated = pred0.isNegated();
                            if (_isNegated) {
                              guard.append((("not (" + baseGuard) + ")"));
                            } else {
                              guard.append((("(" + baseGuard) + ")"));
                            }
                          }
                          int _size = c.getAtomicPredicate().size();
                          boolean _greaterThan_1 = (_size > 1);
                          if (_greaterThan_1) {
                            int _size_1 = c.getAtomicPredicate().size();
                            int _minus_1 = (_size_1 - 1);
                            ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _minus_1, true);
                            for (final Integer i_2 : _doubleDotLessThan) {
                              {
                                final AtomicPredicate predNext = c.getAtomicPredicate().get(((i_2).intValue() + 1));
                                final String operator = c.getOp().get((i_2).intValue()).toString();
                                boolean _equals_1 = Objects.equals(operator, "&&");
                                if (_equals_1) {
                                  guard.append(" and(");
                                } else {
                                  boolean _equals_2 = Objects.equals(operator, "||");
                                  if (_equals_2) {
                                    guard.append(" or(");
                                  }
                                }
                                final String nextGuard = this.apPositionGuard(model, 
                                  predNext.getAgent().getName().toString(), 
                                  predNext.getLocation().getName().toString());
                                boolean _isNegated_1 = predNext.isNegated();
                                if (_isNegated_1) {
                                  guard.append((("\n\tnot (" + nextGuard) + "))"));
                                } else {
                                  guard.append((("\n\t(" + nextGuard) + "))"));
                                }
                              }
                            }
                          }
                        }
                      }
                      if (isHuman) {
                        StringConcatenation _builder_2 = new StringConcatenation();
                        _builder_2.append("_h_");
                        String _string = actionC_1.getActionType().toString();
                        _builder_2.append(_string);
                        _builder_2.append("[H-1]!");
                        sync = _builder_2.toString();
                      } else {
                        if (isRobot) {
                          StringConcatenation _builder_3 = new StringConcatenation();
                          _builder_3.append("_r_");
                          String _string_1 = actionC_1.getActionType().toString();
                          _builder_3.append(_string_1);
                          _builder_3.append("[R-1]!");
                          sync = _builder_3.toString();
                        }
                      }
                      if (isHuman) {
                        update.append(this.getActionUpdate(model, actionC_1, "human", "H-1"));
                      } else {
                        if (isRobot) {
                          update.append(this.getActionUpdate(model, actionC_1, "rob", "R-1"));
                        }
                      }
                      String _cs1 = cs.getCs1();
                      boolean _tripleNotEquals = (_cs1 != null);
                      if (_tripleNotEquals) {
                        int _get = firstActionID[subSequence_1.getI()];
                        int _plus_2 = (_get + (actionCount * 2));
                        int _minus_1 = (_plus_2 - 2);
                        source = _minus_1;
                        int _get_1 = firstActionID[subSequence_1.getI()];
                        int _plus_3 = (_get_1 + (actionCount * 2));
                        int _minus_2 = (_plus_3 - 1);
                        target = _minus_2;
                        select = null;
                        controllable = true;
                        x = ((xOffset * ((actionCount * 2) - 2)) + xOffset);
                        int _i_4 = subSequence_1.getI();
                        int _multiply_1 = (_i_4 * yOffset);
                        int _minus_3 = (_multiply_1 - 100);
                        y = _minus_3;
                        nailX = (((xOffset * ((actionCount * 2) - 2)) + xOffset) + 100);
                        int _i_5 = subSequence_1.getI();
                        int _multiply_2 = (_i_5 * yOffset);
                        int _minus_4 = (_multiply_2 - 100);
                        nailY = _minus_4;
                        transition.append(this.genTran(id, source, target, select, guard.toString(), ("stop" + sync), null, controllable, x, y, nailX, nailY));
                        id++;
                      }
                      String _cs2 = cs.getCs2();
                      boolean _tripleNotEquals_1 = (_cs2 != null);
                      if (_tripleNotEquals_1) {
                        String _string_2 = cs.getAction().getActionType().toString();
                        boolean _equals = Objects.equals(_string_2, "stop");
                        if (_equals) {
                          locName = String.format("A%d_a%d_%s", Integer.valueOf(AgentID), Integer.valueOf(actionID), cs.getAction().getActionType().toString());
                          invariant = null;
                          rate_of_exp = null;
                          urgent = false;
                          committed = false;
                          x = (((actionCount * xOffset) * 2) - xOffset);
                          int _i_6 = subSequence_1.getI();
                          int _multiply_3 = (_i_6 * yOffset);
                          int _minus_5 = (_multiply_3 - (yOffset / 2));
                          y = _minus_5;
                          builder.append(this.genLoc(id, x, y, locName, invariant, rate_of_exp, urgent, committed));
                          id++;
                          actionID++;
                          int _get_2 = firstActionID[subSequence_1.getI()];
                          int _plus_4 = (_get_2 + (actionCount * 2));
                          int _minus_6 = (_plus_4 - 2);
                          source = _minus_6;
                          target = (id - 1);
                          select = null;
                          controllable = true;
                          x = (((xOffset * actionCount) * 2) + xOffset);
                          int _i_7 = subSequence_1.getI();
                          int _multiply_4 = (_i_7 * yOffset);
                          int _minus_7 = (_multiply_4 - 100);
                          y = _minus_7;
                          nailX = 0;
                          nailY = 0;
                          String _string_3 = guard.toString();
                          String _plus_5 = ("not (" + _string_3);
                          String _plus_6 = (_plus_5 + ")");
                          transition.append(this.genTran(id, source, target, select, _plus_6, ("stop" + sync), null, controllable, x, y, nailX, nailY));
                          id++;
                          if ((actionCount == 1)) {
                            source = (id - 2);
                            target = subSequence_1.getI();
                            select = null;
                            sync = ("stop" + sync);
                            controllable = true;
                            x = (((xOffset * actionCount) * 2) + xOffset);
                            int _i_8 = subSequence_1.getI();
                            int _multiply_5 = (_i_8 * yOffset);
                            int _minus_8 = (_multiply_5 - 100);
                            y = _minus_8;
                            nailX = 0;
                            nailY = 0;
                            transition.append(this.genTran(id, source, target, select, guard.toString(), sync, update.toString(), controllable, x, y, nailX, nailY));
                            id++;
                          } else {
                            source = (id - 2);
                            int _get_3 = firstActionID[subSequence_1.getI()];
                            int _plus_7 = (_get_3 + (actionCount * 2));
                            int _minus_9 = (_plus_7 - 3);
                            target = _minus_9;
                            select = null;
                            sync = ("stop" + sync);
                            controllable = true;
                            x = (((xOffset * actionCount) * 2) + xOffset);
                            int _i_9 = subSequence_1.getI();
                            int _multiply_6 = (_i_9 * yOffset);
                            int _minus_10 = (_multiply_6 - 100);
                            y = _minus_10;
                            nailX = 0;
                            nailY = 0;
                            transition.append(this.genTran(id, source, target, select, guard.toString(), sync, update.toString(), controllable, x, y, nailX, nailY));
                            id++;
                          }
                        }
                        String _string_4 = cs.getAction().getActionType().toString();
                        boolean _equals_1 = Objects.equals(_string_4, "moveTo");
                        if (_equals_1) {
                        }
                      }
                      String _cs3 = cs.getCs3();
                      boolean _tripleNotEquals_2 = (_cs3 != null);
                      if (_tripleNotEquals_2) {
                        locName = String.format("A%d_a%d_%s", Integer.valueOf(AgentID), Integer.valueOf(actionID), cs.getAction().getActionType().toString());
                        invariant = null;
                        rate_of_exp = null;
                        urgent = false;
                        committed = false;
                        builder.append(this.genLoc(id, (x - xOffset), (y - (yOffset / 2)), locName, invariant, rate_of_exp, urgent, committed));
                        id++;
                        actionID++;
                      }
                      String _cs4 = cs.getCs4();
                      boolean _tripleNotEquals_3 = (_cs4 != null);
                      if (_tripleNotEquals_3) {
                      }
                      guard.setLength(0);
                      sync = null;
                      update.setLength(0);
                    }
                  }
                }
              }
            }
            int _i_4 = subSequence_1.getI();
            int _minus_1 = (_i_4 - 1);
            int _get = emptySS[_minus_1];
            boolean _equals = (_get == 1);
            if (_equals) {
              int _size = subSequence_1.getActionC().size();
              int _multiply_1 = (_size * 2);
              int _multiply_2 = (_multiply_1 * xOffset);
              int _plus_2 = (_multiply_2 + xOffset);
              x = _plus_2;
              int _i_5 = subSequence_1.getI();
              int _multiply_3 = (_i_5 * yOffset);
              y = _multiply_3;
              locName = String.format("s%d_end", Integer.valueOf(subSequence_1.getI()));
              invariant = null;
              rate_of_exp = null;
              urgent = false;
              committed = false;
              builder.append(this.genLoc(id, x, y, locName, invariant, rate_of_exp, urgent, committed));
              source = lastActionID[subSequence_1.getI()];
              target = id;
              select = null;
              sync = null;
              controllable = true;
              nailX = 0;
              nailY = 0;
              transition.append(this.genTran((id + 1), source, target, select, null, sync, String.format("%s_stat = true", sequence.getAgent().getName().toLowerCase()), controllable, (x - xOffset), y, nailX, nailY));
              endSubSeqID[subSequence_1.getI()] = id;
              id = (id + 2);
            }
          }
        }
        IntegerRange _upTo_2 = new IntegerRange(1, (maxIndex).intValue());
        for (final Integer i_1 : _upTo_2) {
          {
            int _get = emptySS[((i_1).intValue() - 1)];
            boolean _equals = (_get == 0);
            if (_equals) {
              x = xOffset;
              y = ((i_1).intValue() * yOffset);
              locName = String.format("s%d_idle", i_1);
              invariant = null;
              rate_of_exp = null;
              urgent = false;
              committed = false;
              builder.append(this.genLoc(id, x, y, locName, invariant, rate_of_exp, urgent, committed));
              endSubSeqID[(i_1).intValue()] = id;
              id++;
            }
            emptySS[((i_1).intValue() - 1)] = 0;
          }
        }
        EList<SubSequence> _subSequence_2 = sequence.getSubSequence();
        for (final SubSequence subSequence_2 : _subSequence_2) {
          int _j_2 = subSequence_2.getJ();
          int _i_2 = subSequence_2.getI();
          int _minus_1 = (_j_2 - _i_2);
          boolean _greaterThan_1 = (_minus_1 > 0);
          if (_greaterThan_1) {
            int _j_3 = subSequence_2.getJ();
            boolean _notEquals = (_j_3 != 0);
            if (_notEquals) {
              int _i_3 = subSequence_2.getI();
              int _plus_1 = (_i_3 + 1);
              int _j_4 = subSequence_2.getJ();
              IntegerRange _upTo_3 = new IntegerRange(_plus_1, _j_4);
              for (final Integer i_2 : _upTo_3) {
                endSubSeqID[(i_2).intValue()] = endSubSeqID[((i_2).intValue() - 1)];
              }
            }
          }
        }
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("\t\t");
        _builder_2.append("<init ref=\"id0\"/>");
        _builder_2.newLine();
        builder.append(_builder_2);
        int firstAction = 0;
        actionCount = 0;
        EList<SubSequence> _subSequence_3 = sequence.getSubSequence();
        for (final SubSequence subSequence_3 : _subSequence_3) {
          {
            int _j_5 = subSequence_3.getJ();
            boolean _notEquals_1 = (_j_5 != 0);
            if (_notEquals_1) {
              int _i_4 = subSequence_3.getI();
              int _plus_2 = (_i_4 + 1);
              int _j_6 = subSequence_3.getJ();
              IntegerRange _upTo_4 = new IntegerRange(_plus_2, _j_6);
              for (final Integer i_3 : _upTo_4) {
                emptySS[((i_3).intValue() - 1)] = 1;
              }
            }
            firstAction = 1;
            EList<ActionC> _actionC = subSequence_3.getActionC();
            for (final ActionC actionC : _actionC) {
              {
                int _i_5 = subSequence_3.getI();
                int _minus_2 = (_i_5 - 1);
                emptySS[_minus_2] = 1;
                if ((firstAction == 1)) {
                  firstAction = 0;
                  String _xifexpression = null;
                  if (isHuman) {
                    _xifexpression = "human";
                  } else {
                    _xifexpression = "rob";
                  }
                  final String agentPrefix = _xifexpression;
                  String _xifexpression_1 = null;
                  if (isHuman) {
                    _xifexpression_1 = "H-1";
                  } else {
                    _xifexpression_1 = "R-1";
                  }
                  final String agentIndex = _xifexpression_1;
                  final LIRAsGenerator.SeqActionConfig config = this.getSeqActionConfig(actionC.getActionType().toString(), agentPrefix);
                  if ((config != null)) {
                    int _i_6 = subSequence_3.getI();
                    int _get = firstActionID[subSequence_3.getI()];
                    String _xifexpression_2 = null;
                    if (config.hasBusyLoop) {
                      _xifexpression_2 = "t = 0";
                    } else {
                      _xifexpression_2 = null;
                    }
                    int _i_7 = subSequence_3.getI();
                    int _multiply = (_i_7 * yOffset);
                    transition.append(this.genTran(id, _i_6, _get, null, null, config.startSync, _xifexpression_2, true, 0, _multiply, 0, 0));
                    id++;
                    if (config.hasInitialUpdate) {
                      update.append(this.getActionUpdate(model, actionC, agentPrefix, agentIndex));
                    }
                    int _i_8 = subSequence_3.getI();
                    int _minus_3 = (_i_8 - 1);
                    int _get_1 = endSubSeqID[_minus_3];
                    int _i_9 = subSequence_3.getI();
                    String _format = String.format("s%d?", Integer.valueOf(subSequence_3.getI()));
                    String _string = update.toString();
                    int _i_10 = subSequence_3.getI();
                    int _multiply_1 = (_i_10 * yOffset);
                    int _minus_4 = (_multiply_1 - 100);
                    transition.append(this.genTran(id, _get_1, _i_9, null, null, _format, _string, true, 0, _minus_4, 0, 0));
                    id++;
                    update.setLength(0);
                    if (config.hasBusyLoop) {
                      int _get_2 = firstActionID[subSequence_3.getI()];
                      int _i_11 = subSequence_3.getI();
                      int _i_12 = subSequence_3.getI();
                      int _multiply_2 = (_i_12 * yOffset);
                      int _plus_3 = (_multiply_2 + 100);
                      int _i_13 = subSequence_3.getI();
                      int _multiply_3 = (_i_13 * yOffset);
                      int _plus_4 = (_multiply_3 + 80);
                      transition.append(this.genTran(id, _get_2, _i_11, null, "t&gt;=5", null, null, true, ((xOffset * actionCount) * 2), _plus_3, (((xOffset * actionCount) * 2) + 100), _plus_4));
                      id++;
                    }
                    final String actionGuard = this.getActionGuard(model, actionC, agentPrefix, agentIndex);
                    boolean _isEmpty = actionGuard.isEmpty();
                    boolean _not = (!_isEmpty);
                    if (_not) {
                      guard.append(actionGuard);
                    }
                    final ActionC nextAction = this.getNextAction(subSequence_3.getActionC(), actionC);
                    if ((nextAction != null)) {
                      final String nextUpdate = this.getActionUpdate(model, nextAction, agentPrefix, agentIndex);
                      boolean _isEmpty_1 = nextUpdate.isEmpty();
                      boolean _not_1 = (!_isEmpty_1);
                      if (_not_1) {
                        update.append(nextUpdate);
                      }
                    }
                    int _get_3 = firstActionID[subSequence_3.getI()];
                    int _get_4 = firstActionID[subSequence_3.getI()];
                    int _plus_5 = (_get_4 + 1);
                    String _string_1 = guard.toString();
                    String _string_2 = update.toString();
                    int _i_14 = subSequence_3.getI();
                    int _multiply_4 = (_i_14 * yOffset);
                    int _minus_5 = (_multiply_4 - 100);
                    transition.append(this.genTran(id, _get_3, _plus_5, null, _string_1, config.stopSync, _string_2, true, xOffset, _minus_5, 0, 0));
                    id++;
                    guard.setLength(0);
                    update.setLength(0);
                  }
                } else {
                  actionCount++;
                  String _xifexpression_3 = null;
                  if (isHuman) {
                    _xifexpression_3 = "human";
                  } else {
                    _xifexpression_3 = "rob";
                  }
                  final String agentPrefix_1 = _xifexpression_3;
                  String _xifexpression_4 = null;
                  if (isHuman) {
                    _xifexpression_4 = "H-1";
                  } else {
                    _xifexpression_4 = "R-1";
                  }
                  final String agentIndex_1 = _xifexpression_4;
                  final LIRAsGenerator.SeqActionConfig config_1 = this.getSeqActionConfig(actionC.getActionType().toString(), agentPrefix_1);
                  if ((config_1 != null)) {
                    int _get_5 = firstActionID[subSequence_3.getI()];
                    int _plus_6 = (_get_5 + (actionCount * 2));
                    int _minus_6 = (_plus_6 - 1);
                    int _get_6 = firstActionID[subSequence_3.getI()];
                    int _plus_7 = (_get_6 + (actionCount * 2));
                    String _xifexpression_5 = null;
                    if (config_1.hasBusyLoop) {
                      _xifexpression_5 = "t = 0";
                    } else {
                      _xifexpression_5 = null;
                    }
                    int _i_15 = subSequence_3.getI();
                    int _multiply_5 = (_i_15 * yOffset);
                    transition.append(
                      this.genTran(id, _minus_6, _plus_7, 
                        null, 
                        null, 
                        config_1.startSync, _xifexpression_5, 
                        true, 
                        ((xOffset * actionCount) * 2), _multiply_5, 
                        0, 
                        0));
                    id++;
                    if (config_1.hasBusyLoop) {
                      int _get_7 = firstActionID[subSequence_3.getI()];
                      int _plus_8 = (_get_7 + (actionCount * 2));
                      int _get_8 = firstActionID[subSequence_3.getI()];
                      int _plus_9 = (_get_8 + (actionCount * 2));
                      int _minus_7 = (_plus_9 - 1);
                      int _i_16 = subSequence_3.getI();
                      int _multiply_6 = (_i_16 * yOffset);
                      int _plus_10 = (_multiply_6 + 100);
                      int _i_17 = subSequence_3.getI();
                      int _multiply_7 = (_i_17 * yOffset);
                      int _plus_11 = (_multiply_7 + 80);
                      transition.append(
                        this.genTran(id, _plus_8, _minus_7, 
                          null, 
                          "t&gt;=5", 
                          null, 
                          null, 
                          true, 
                          ((xOffset * actionCount) * 2), _plus_10, 
                          (((xOffset * actionCount) * 2) + 100), _plus_11));
                      id++;
                    }
                    final String actionGuard_1 = this.getActionGuard(model, actionC, agentPrefix_1, agentIndex_1);
                    boolean _isEmpty_2 = actionGuard_1.isEmpty();
                    boolean _not_2 = (!_isEmpty_2);
                    if (_not_2) {
                      guard.append(actionGuard_1);
                    }
                    final ActionC nextAction_1 = this.getNextAction(subSequence_3.getActionC(), actionC);
                    if ((nextAction_1 != null)) {
                      final String nextUpdate_1 = this.getActionUpdate(model, nextAction_1, agentPrefix_1, agentIndex_1);
                      boolean _isEmpty_3 = nextUpdate_1.isEmpty();
                      boolean _not_3 = (!_isEmpty_3);
                      if (_not_3) {
                        update.append(nextUpdate_1);
                      }
                    }
                    int _get_9 = firstActionID[subSequence_3.getI()];
                    int _plus_12 = (_get_9 + (actionCount * 2));
                    int _get_10 = firstActionID[subSequence_3.getI()];
                    int _plus_13 = (_get_10 + (actionCount * 2));
                    int _plus_14 = (_plus_13 + 1);
                    String _string_3 = guard.toString();
                    String _string_4 = update.toString();
                    int _i_18 = subSequence_3.getI();
                    int _multiply_8 = (_i_18 * yOffset);
                    int _minus_8 = (_multiply_8 - 100);
                    transition.append(
                      this.genTran(id, _plus_12, _plus_14, 
                        null, _string_3, 
                        config_1.stopSync, _string_4, 
                        true, 
                        (xOffset * ((actionCount * 2) + 1)), _minus_8, 
                        0, 
                        0));
                    id++;
                    guard.setLength(0);
                    update.setLength(0);
                  }
                }
              }
            }
            actionCount = 0;
          }
        }
        IntegerRange _upTo_4 = new IntegerRange(1, (maxIndex).intValue());
        for (final Integer i_3 : _upTo_4) {
          {
            int _get = emptySS[((i_3).intValue() - 1)];
            boolean _equals = (_get == 0);
            if (_equals) {
              x = xOffset;
              y = ((i_3).intValue() * yOffset);
              transition.append(this.genTran(id, endSubSeqID[((i_3).intValue() - 1)], (i_3).intValue(), null, null, String.format("s%d?", i_3), null, true, 0, (((i_3).intValue() * yOffset) - 100), 0, 0));
              id++;
              transition.append(this.genTran(id, (i_3).intValue(), endSubSeqID[(i_3).intValue()], null, null, null, String.format("%s_stat = true", sequence.getAgent().getName().toLowerCase()), true, 0, ((i_3).intValue() * yOffset), 0, 0));
              id++;
            }
            emptySS[((i_3).intValue() - 1)] = 0;
          }
        }
        transition.append(this.genTran(id, endSubSeqID[(maxIndex).intValue()], endID, null, null, "e?", null, true, 0, ((((maxIndex).intValue() + 1) * yOffset) - 100), 0, 0));
        id++;
        builder.append(transition.toString());
        transition.setLength(0);
        builder.append("\t</template>\n");
      }
    }
    return builder.toString();
  }

  public int getTargetIndex(final Model model, final String targetName) {
    final Function1<Location, String> _function = (Location it) -> {
      return it.getName().toString();
    };
    final List<String> locations = IterableExtensions.<String>toList(ListExtensions.<Location, String>map(model.getPattern().getLocation(), _function));
    final int locIndex = locations.indexOf(targetName);
    if ((locIndex != (-1))) {
      return locIndex;
    }
    final Function1<it.polimi.lIRAs.Resource, String> _function_1 = (it.polimi.lIRAs.Resource it) -> {
      return it.getName().toString();
    };
    final List<String> resources = IterableExtensions.<String>toList(ListExtensions.<it.polimi.lIRAs.Resource, String>map(model.getPattern().getResources(), _function_1));
    final int resIndex = resources.indexOf(targetName);
    if ((resIndex != (-1))) {
      int _size = locations.size();
      return (_size + resIndex);
    }
    return (-1);
  }

  public boolean supportsAction(final String agentPrefix, final String actionType) {
    boolean _equals = Objects.equals(agentPrefix, "human");
    if (_equals) {
      return Objects.equals(actionType, "moveTo");
    }
    boolean _equals_1 = Objects.equals(agentPrefix, "rob");
    if (_equals_1) {
      return (((Objects.equals(actionType, "moveTo") || 
        Objects.equals(actionType, "follow")) || 
        Objects.equals(actionType, "pick")) || 
        Objects.equals(actionType, "deposit"));
    }
    return false;
  }

  public String getActionUpdate(final Model model, final ActionC action, final String agentPrefix, final String agentUppaalIndex) {
    final String actionType = action.getActionType().toString();
    boolean _supportsAction = this.supportsAction(agentPrefix, actionType);
    boolean _not = (!_supportsAction);
    if (_not) {
      return "";
    }
    if (actionType != null) {
      switch (actionType) {
        case "moveTo":
          final int targetIndex = this.getTargetIndex(model, action.getTarget().getName().toString());
          String _xifexpression = null;
          boolean _equals = Objects.equals(agentPrefix, "human");
          if (_equals) {
            _xifexpression = "human_loc_target";
          } else {
            _xifexpression = "robot_loc_target";
          }
          final String targetVar = _xifexpression;
          StringConcatenation _builder = new StringConcatenation();
          _builder.append(targetVar);
          _builder.append("[");
          _builder.append(agentUppaalIndex);
          _builder.append("] = ");
          _builder.append(targetIndex);
          return _builder.toString();
        case "pick":
          final Function1<it.polimi.lIRAs.Resource, String> _function = (it.polimi.lIRAs.Resource it) -> {
            return it.getName().toString();
          };
          final int resIndex = IterableExtensions.<String>toList(ListExtensions.<it.polimi.lIRAs.Resource, String>map(model.getPattern().getResources(), _function)).indexOf(action.getResource().getName().toString());
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("robot_res_to_pick[");
          _builder_1.append(agentUppaalIndex);
          _builder_1.append("] = ");
          _builder_1.append(resIndex);
          _builder_1.append(", old_inventory = robot_inventory");
          return _builder_1.toString();
        case "deposit":
          final Function1<it.polimi.lIRAs.Resource, String> _function_1 = (it.polimi.lIRAs.Resource it) -> {
            return it.getName().toString();
          };
          final int resIndex_1 = IterableExtensions.<String>toList(ListExtensions.<it.polimi.lIRAs.Resource, String>map(model.getPattern().getResources(), _function_1)).indexOf(action.getResource().getName().toString());
          StringConcatenation _builder_2 = new StringConcatenation();
          _builder_2.append("robot_res_to_deposit[");
          _builder_2.append(agentUppaalIndex);
          _builder_2.append("] = ");
          _builder_2.append(resIndex_1);
          _builder_2.append(", old_inventory = robot_inventory");
          return _builder_2.toString();
        case "follow":
          final Function1<Location, String> _function_2 = (Location it) -> {
            return it.getName().toString();
          };
          final int locIndex = IterableExtensions.<String>toList(ListExtensions.<Location, String>map(model.getPattern().getLocation(), _function_2)).indexOf(action.getTarget().getName().toString());
          final Pair<String, Integer> targetAgentData = this.resolveAgentPositionData(model, action.getAgent().getName().toString());
          int _xifexpression_1 = (int) 0;
          String _key = null;
          if (targetAgentData!=null) {
            _key=targetAgentData.getKey();
          }
          boolean _equals_1 = Objects.equals(_key, "human");
          if (_equals_1) {
            Integer _value = targetAgentData.getValue();
            _xifexpression_1 = ((_value).intValue() + 1);
          } else {
            _xifexpression_1 = 0;
          }
          final int humanFollowVar = _xifexpression_1;
          int _xifexpression_2 = (int) 0;
          String _key_1 = null;
          if (targetAgentData!=null) {
            _key_1=targetAgentData.getKey();
          }
          boolean _equals_2 = Objects.equals(_key_1, "rob");
          if (_equals_2) {
            Integer _value_1 = targetAgentData.getValue();
            _xifexpression_2 = ((_value_1).intValue() + 1);
          } else {
            _xifexpression_2 = 0;
          }
          final int robotFollowVar = _xifexpression_2;
          StringConcatenation _builder_3 = new StringConcatenation();
          _builder_3.append("f_loc_target[");
          _builder_3.append(agentUppaalIndex);
          _builder_3.append("] = ");
          _builder_3.append(locIndex);
          _builder_3.append(",");
          _builder_3.newLineIfNotEmpty();
          _builder_3.append("human_to_be_followed[");
          _builder_3.append(agentUppaalIndex);
          _builder_3.append("] = ");
          _builder_3.append(humanFollowVar);
          _builder_3.append(",");
          _builder_3.newLineIfNotEmpty();
          _builder_3.append("robot_to_be_followed[");
          _builder_3.append(agentUppaalIndex);
          _builder_3.append("] = ");
          _builder_3.append(robotFollowVar);
          return _builder_3.toString();
        default:
          return "";
      }
    } else {
      return "";
    }
  }

  public String getActionGuard(final Model model, final ActionC action, final String agentPrefix, final String agentUppaalIndex) {
    final String actionType = action.getActionType().toString();
    final int tolerance = 30;
    boolean _supportsAction = this.supportsAction(agentPrefix, actionType);
    boolean _not = (!_supportsAction);
    if (_not) {
      return "";
    }
    if (actionType != null) {
      switch (actionType) {
        case "moveTo":
          String _xifexpression = null;
          boolean _equals = Objects.equals(agentPrefix, "human");
          if (_equals) {
            _xifexpression = "human_loc_target";
          } else {
            _xifexpression = "robot_loc_target";
          }
          final String targetVar = _xifexpression;
          StringConcatenation _builder = new StringConcatenation();
          _builder.append(agentPrefix);
          _builder.append("PositionX[");
          _builder.append(agentUppaalIndex);
          _builder.append("] &lt;= location_X[");
          _builder.append(targetVar);
          _builder.append("[");
          _builder.append(agentUppaalIndex);
          _builder.append("]]+");
          _builder.append(tolerance);
          _builder.append(" and");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append(agentPrefix, "\t");
          _builder.append("PositionX[");
          _builder.append(agentUppaalIndex, "\t");
          _builder.append("] &gt;= location_X[");
          _builder.append(targetVar, "\t");
          _builder.append("[");
          _builder.append(agentUppaalIndex, "\t");
          _builder.append("]]-");
          _builder.append(tolerance, "\t");
          _builder.append(" and");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append(agentPrefix, "\t");
          _builder.append("PositionY[");
          _builder.append(agentUppaalIndex, "\t");
          _builder.append("] &lt;= location_Y[");
          _builder.append(targetVar, "\t");
          _builder.append("[");
          _builder.append(agentUppaalIndex, "\t");
          _builder.append("]]+");
          _builder.append(tolerance, "\t");
          _builder.append(" and");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append(agentPrefix, "\t");
          _builder.append("PositionY[");
          _builder.append(agentUppaalIndex, "\t");
          _builder.append("] &gt;= location_Y[");
          _builder.append(targetVar, "\t");
          _builder.append("[");
          _builder.append(agentUppaalIndex, "\t");
          _builder.append("]]-");
          _builder.append(tolerance, "\t");
          return _builder.toString();
        case "pick":
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("robot_inventory[");
          _builder_1.append(agentUppaalIndex);
          _builder_1.append("][robot_res_to_pick[");
          _builder_1.append(agentUppaalIndex);
          _builder_1.append("]] > old_inventory[");
          _builder_1.append(agentUppaalIndex);
          _builder_1.append("][robot_res_to_pick[");
          _builder_1.append(agentUppaalIndex);
          _builder_1.append("]]");
          return _builder_1.toString();
        case "deposit":
          StringConcatenation _builder_2 = new StringConcatenation();
          _builder_2.append("robot_inventory[");
          _builder_2.append(agentUppaalIndex);
          _builder_2.append("][robot_res_to_deposit[");
          _builder_2.append(agentUppaalIndex);
          _builder_2.append("]] &lt; old_inventory[");
          _builder_2.append(agentUppaalIndex);
          _builder_2.append("][robot_res_to_deposit[");
          _builder_2.append(agentUppaalIndex);
          _builder_2.append("]]");
          return _builder_2.toString();
        case "follow":
          final Pair<String, Integer> targetAgentData = this.resolveAgentPositionData(model, action.getAgent().getName().toString());
          if ((targetAgentData == null)) {
            return "// Error: Target agent not found";
          }
          final String tPrefix = targetAgentData.getKey();
          String _xifexpression_1 = null;
          boolean _equals_1 = Objects.equals(tPrefix, "human");
          if (_equals_1) {
            _xifexpression_1 = "human_to_be_followed";
          } else {
            _xifexpression_1 = "robot_to_be_followed";
          }
          final String tVar = _xifexpression_1;
          StringConcatenation _builder_3 = new StringConcatenation();
          _builder_3.append(tPrefix);
          _builder_3.append("PositionX[");
          _builder_3.append(tVar);
          _builder_3.append("[");
          _builder_3.append(agentUppaalIndex);
          _builder_3.append("]-1] &lt;= location_X[f_loc_target[");
          _builder_3.append(agentUppaalIndex);
          _builder_3.append("]]+");
          _builder_3.append(tolerance);
          _builder_3.append(" and");
          _builder_3.newLineIfNotEmpty();
          _builder_3.append("\t");
          _builder_3.append(tPrefix, "\t");
          _builder_3.append("PositionX[");
          _builder_3.append(tVar, "\t");
          _builder_3.append("[");
          _builder_3.append(agentUppaalIndex, "\t");
          _builder_3.append("]-1] &gt;= location_X[f_loc_target[");
          _builder_3.append(agentUppaalIndex, "\t");
          _builder_3.append("]]-");
          _builder_3.append(tolerance, "\t");
          _builder_3.append(" and");
          _builder_3.newLineIfNotEmpty();
          _builder_3.append("\t");
          _builder_3.append(tPrefix, "\t");
          _builder_3.append("PositionY[");
          _builder_3.append(tVar, "\t");
          _builder_3.append("[");
          _builder_3.append(agentUppaalIndex, "\t");
          _builder_3.append("]-1] &lt;= location_Y[f_loc_target[");
          _builder_3.append(agentUppaalIndex, "\t");
          _builder_3.append("]]+");
          _builder_3.append(tolerance, "\t");
          _builder_3.append(" and");
          _builder_3.newLineIfNotEmpty();
          _builder_3.append("\t");
          _builder_3.append(tPrefix, "\t");
          _builder_3.append("PositionY[");
          _builder_3.append(tVar, "\t");
          _builder_3.append("[");
          _builder_3.append(agentUppaalIndex, "\t");
          _builder_3.append("]-1] &gt;= location_Y[f_loc_target[");
          _builder_3.append(agentUppaalIndex, "\t");
          _builder_3.append("]]-");
          _builder_3.append(tolerance, "\t");
          _builder_3.append(" and");
          _builder_3.newLineIfNotEmpty();
          _builder_3.append("\t");
          _builder_3.append(agentPrefix, "\t");
          _builder_3.append("PositionX[");
          _builder_3.append(agentUppaalIndex, "\t");
          _builder_3.append("] &gt;= location_X[f_loc_target[");
          _builder_3.append(agentUppaalIndex, "\t");
          _builder_3.append("]]-");
          _builder_3.append(tolerance, "\t");
          _builder_3.append(" and");
          _builder_3.newLineIfNotEmpty();
          _builder_3.append("\t");
          _builder_3.append(agentPrefix, "\t");
          _builder_3.append("PositionX[");
          _builder_3.append(agentUppaalIndex, "\t");
          _builder_3.append("] &lt;= location_X[f_loc_target[");
          _builder_3.append(agentUppaalIndex, "\t");
          _builder_3.append("]]+");
          _builder_3.append(tolerance, "\t");
          _builder_3.append(" and");
          _builder_3.newLineIfNotEmpty();
          _builder_3.append("\t");
          _builder_3.append(agentPrefix, "\t");
          _builder_3.append("PositionY[");
          _builder_3.append(agentUppaalIndex, "\t");
          _builder_3.append("] &gt;= location_Y[f_loc_target[");
          _builder_3.append(agentUppaalIndex, "\t");
          _builder_3.append("]]-");
          _builder_3.append(tolerance, "\t");
          _builder_3.append(" and");
          _builder_3.newLineIfNotEmpty();
          _builder_3.append("\t");
          _builder_3.append(agentPrefix, "\t");
          _builder_3.append("PositionY[");
          _builder_3.append(agentUppaalIndex, "\t");
          _builder_3.append("] &lt;= location_Y[f_loc_target[");
          _builder_3.append(agentUppaalIndex, "\t");
          _builder_3.append("]]+");
          _builder_3.append(tolerance, "\t");
          return _builder_3.toString();
        default:
          return "";
      }
    } else {
      return "";
    }
  }

  public Pair<String, Integer> resolveAgentPositionData(final Model model, final String agentName) {
    final Function1<Human, String> _function = (Human it) -> {
      return it.getName();
    };
    final int hIndex = IterableExtensions.<String>toList(ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function)).indexOf(agentName);
    if ((hIndex != (-1))) {
      return Pair.<String, Integer>of("human", Integer.valueOf(hIndex));
    }
    final Function1<Robot, String> _function_1 = (Robot it) -> {
      return it.getName();
    };
    final int rIndex = IterableExtensions.<String>toList(ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_1)).indexOf(agentName);
    if ((rIndex != (-1))) {
      return Pair.<String, Integer>of("rob", Integer.valueOf(rIndex));
    }
    return null;
  }

  public String apPositionGuard(final Model model, final String agentName, final String targetName) {
    final int tolerance = 30;
    final int targetIndex = this.getTargetIndex(model, targetName);
    final Pair<String, Integer> agentData = this.resolveAgentPositionData(model, agentName);
    if (((agentData != null) && (targetIndex != (-1)))) {
      final String prefix = agentData.getKey();
      final Integer index = agentData.getValue();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(prefix);
      _builder.append("PositionX[");
      _builder.append(index);
      _builder.append("] &lt;= location_X[");
      _builder.append(targetIndex);
      _builder.append("]+");
      _builder.append(tolerance);
      _builder.append(" and");
      _builder.newLineIfNotEmpty();
      _builder.append(prefix);
      _builder.append("PositionX[");
      _builder.append(index);
      _builder.append("] &gt;= location_X[");
      _builder.append(targetIndex);
      _builder.append("]-");
      _builder.append(tolerance);
      _builder.append(" and");
      _builder.newLineIfNotEmpty();
      _builder.append(prefix);
      _builder.append("PositionY[");
      _builder.append(index);
      _builder.append("] &lt;= location_Y[");
      _builder.append(targetIndex);
      _builder.append("]+");
      _builder.append(tolerance);
      _builder.append(" and");
      _builder.newLineIfNotEmpty();
      _builder.append(prefix);
      _builder.append("PositionY[");
      _builder.append(index);
      _builder.append("] &gt;= location_Y[");
      _builder.append(targetIndex);
      _builder.append("]-");
      _builder.append(tolerance);
      return _builder.toString();
    }
    return "// Error: Agent or Target not found";
  }

  public String genLoc(final int id, final int x, final int y, final String name, final String invariant, final String rate_of_exp, final boolean urgent, final boolean committed) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<location id=\"id");
    _builder.append(id);
    _builder.append("\" x=\"");
    _builder.append(x);
    _builder.append("\" y=\"");
    _builder.append(y);
    _builder.append("\">");
    _builder.newLineIfNotEmpty();
    {
      if ((name != null)) {
        _builder.append("\t");
        _builder.append("<name x=\"");
        _builder.append((x - 17), "\t");
        _builder.append("\" y=\"");
        _builder.append((y - 34), "\t");
        _builder.append("\">");
        _builder.append(name, "\t");
        _builder.append("</name>");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      if ((invariant != null)) {
        _builder.append("\t");
        _builder.append("<label kind=\"invariant\" x=\"");
        _builder.append((x - 10), "\t");
        _builder.append("\" y=\"");
        _builder.append((y + 17), "\t");
        _builder.append("\">");
        _builder.append(invariant, "\t");
        _builder.append("</label>");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      if ((rate_of_exp != null)) {
        _builder.append("\t");
        _builder.append("<label kind=\"exponentialrate\" x=\"");
        _builder.append((x - 10), "\t");
        _builder.append("\" y=\"");
        _builder.append((y + 34), "\t");
        _builder.append("\">");
        _builder.append(rate_of_exp, "\t");
        _builder.append("</label>");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      if ((urgent && (!committed))) {
        _builder.append("\t");
        _builder.append("<urgent/>");
        _builder.newLine();
      }
    }
    {
      if ((committed && (!urgent))) {
        _builder.append("\t");
        _builder.append("<committed/>");
        _builder.newLine();
      }
    }
    _builder.append("</location>");
    _builder.newLine();
    return _builder.toString();
  }

  public String genTran(final int id, final int source, final int target, final String select, final String guard, final String sync, final String update, final boolean controllable, final int xOffset, final int yOffset, final int nailX, final int nailY) {
    final boolean isPatternOrch = ((xOffset == 500) && (yOffset == 0));
    int _xifexpression = (int) 0;
    if (isPatternOrch) {
      _xifexpression = (xOffset * source);
    } else {
      _xifexpression = xOffset;
    }
    final int baseX = _xifexpression;
    int _xifexpression_1 = (int) 0;
    if (isPatternOrch) {
      _xifexpression_1 = (yOffset * source);
    } else {
      _xifexpression_1 = yOffset;
    }
    final int baseY = _xifexpression_1;
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<transition id=\"id");
    _builder.append(id);
    _builder.append("\"");
    {
      if ((!controllable)) {
        _builder.append(" controllable=\"false\"");
      }
    }
    _builder.append(">");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("<source ref=\"id");
    _builder.append(source, "\t");
    _builder.append("\"/>");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("<target ref=\"id");
    _builder.append(target, "\t");
    _builder.append("\"/>");
    _builder.newLineIfNotEmpty();
    {
      if ((select != null)) {
        _builder.append("\t");
        _builder.append("<label kind=\"select\" x=\"");
        _builder.append((17 + baseX), "\t");
        _builder.append("\" y=\"");
        _builder.append(((-51) + baseY), "\t");
        _builder.append("\">");
        _builder.append(select, "\t");
        _builder.append("</label>");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      if ((guard != null)) {
        _builder.append("\t");
        _builder.append("<label kind=\"guard\" x=\"");
        _builder.append((18 + baseX), "\t");
        _builder.append("\" y=\"");
        _builder.append(((-34) + baseY), "\t");
        _builder.append("\">");
        _builder.append(guard, "\t");
        _builder.append("</label>");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      if ((sync != null)) {
        _builder.append("\t");
        _builder.append("<label kind=\"synchronisation\" x=\"");
        _builder.append((18 + baseX), "\t");
        _builder.append("\" y=\"");
        _builder.append(((-17) + baseY), "\t");
        _builder.append("\">");
        _builder.append(sync, "\t");
        _builder.append("</label>");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      if ((update != null)) {
        _builder.append("\t");
        _builder.append("<label kind=\"assignment\" x=\"");
        _builder.append((18 + baseX), "\t");
        _builder.append("\" y=\"");
        _builder.append((0 + baseY), "\t");
        _builder.append("\">");
        _builder.append(update, "\t");
        _builder.append("</label>");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      if (((nailX != 0) && (nailY != 0))) {
        _builder.append("\t");
        _builder.append("<nail x=\"");
        _builder.append(nailX, "\t");
        _builder.append("\" y=\"");
        _builder.append(nailY, "\t");
        _builder.append("\"/>");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("</transition>");
    _builder.newLine();
    return _builder.toString();
  }

  public String verifier(final Model model) {
    final int simTime = 700;
    final Pattern pattern = model.getPattern();
    final String patternName = pattern.getName().toString();
    Integer _elvis = null;
    final Function1<Sequence, EList<SubSequence>> _function = (Sequence it) -> {
      return it.getSubSequence();
    };
    final Function1<SubSequence, Integer> _function_1 = (SubSequence it) -> {
      return Integer.valueOf(Math.max(it.getI(), it.getJ()));
    };
    Integer _max = IterableExtensions.<Integer>max(IterableExtensions.<SubSequence, Integer>map(IterableExtensions.<Sequence, SubSequence>flatMap(model.getSequence(), _function), _function_1));
    if (_max != null) {
      _elvis = _max;
    } else {
      _elvis = Integer.valueOf(0);
    }
    final Integer maxIndex = _elvis;
    final ArrayList<String> queries = new ArrayList<String>();
    EList<Sequence> _sequence = model.getSequence();
    for (final Sequence seq : _sequence) {
      {
        final int[] continuation = new int[(maxIndex).intValue()];
        final int[] emptySS = new int[(maxIndex).intValue()];
        final String agent = seq.getAgent().getName().toLowerCase();
        EList<SubSequence> _subSequence = seq.getSubSequence();
        for (final SubSequence ss : _subSequence) {
          {
            int _j = ss.getJ();
            int _i = ss.getI();
            boolean _greaterThan = (_j > _i);
            if (_greaterThan) {
              int _i_1 = ss.getI();
              int _plus = (_i_1 + 1);
              int _j_1 = ss.getJ();
              IntegerRange _upTo = new IntegerRange(_plus, _j_1);
              for (final Integer k : _upTo) {
                {
                  continuation[((k).intValue() - 1)] = 1;
                  emptySS[((k).intValue() - 1)] = 1;
                }
              }
            }
            if (((ss.getActionC() != null) && (!ss.getActionC().isEmpty()))) {
              int _i_2 = ss.getI();
              int _minus = (_i_2 - 1);
              emptySS[_minus] = 1;
            }
          }
        }
        final Function1<Integer, String> _function_2 = (Integer i) -> {
          String _xblockexpression = null;
          {
            final int c = continuation[((i).intValue() - 1)];
            final int e = emptySS[((i).intValue() - 1)];
            String _xifexpression = null;
            if ((c == 0)) {
              String _xblockexpression_1 = null;
              {
                StringConcatenation _builder = new StringConcatenation();
                _builder.append(patternName);
                _builder.append("_");
                _builder.append(agent);
                _builder.append("_def.s");
                _builder.append(i);
                _builder.append("_start");
                final String startState = _builder.toString();
                String _xifexpression_1 = null;
                if ((e == 0)) {
                  StringConcatenation _builder_1 = new StringConcatenation();
                  _builder_1.append(patternName);
                  _builder_1.append("_");
                  _builder_1.append(agent);
                  _builder_1.append("_def.s");
                  _builder_1.append(i);
                  _builder_1.append("_idle");
                  _xifexpression_1 = _builder_1.toString();
                } else {
                  StringConcatenation _builder_2 = new StringConcatenation();
                  _builder_2.append(patternName);
                  _builder_2.append("_");
                  _builder_2.append(agent);
                  _builder_2.append("_def.s");
                  _builder_2.append(i);
                  _builder_2.append("_end");
                  _xifexpression_1 = _builder_2.toString();
                }
                final String endOrIdleState = _xifexpression_1;
                StringConcatenation _builder_3 = new StringConcatenation();
                _builder_3.append(startState);
                _builder_3.append(", ");
                _builder_3.append(endOrIdleState);
                _xblockexpression_1 = _builder_3.toString();
              }
              _xifexpression = _xblockexpression_1;
            } else {
              _xifexpression = null;
            }
            _xblockexpression = _xifexpression;
          }
          return _xblockexpression;
        };
        final String states = IterableExtensions.join(IterableExtensions.<String>filterNull(IterableExtensions.<Integer, String>map(new IntegerRange(1, (maxIndex).intValue()), _function_2)), ", ");
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("<query>");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("<formula>simulate[&lt;=");
        _builder.append(simTime, "\t");
        _builder.append("]{");
        _builder.append(states, "\t");
        _builder.append(", ");
        _builder.append(patternName, "\t");
        _builder.append(".end}</formula>");
        _builder.newLineIfNotEmpty();
        _builder.append("</query>");
        _builder.newLine();
        queries.add(_builder.toString());
      }
    }
    final int hCount = pattern.getHuman().size();
    final int rCount = pattern.getRobot().size();
    final int resCount = pattern.getResources().size();
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<query>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<formula>Pr[&lt;=");
    _builder.append(simTime, "\t");
    _builder.append("] (&lt;&gt; ");
    _builder.append(patternName, "\t");
    _builder.append(".end)</formula>");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("<comment>Check if the whole pattern ends within ");
    _builder.append(simTime, "\t");
    _builder.append(" time unit</comment>");
    _builder.newLineIfNotEmpty();
    _builder.append("</query>");
    _builder.newLine();
    queries.add(_builder.toString());
    final ArrayList<String> posParts = new ArrayList<String>();
    if ((hCount > 0)) {
      final Function1<Integer, String> _function_2 = (Integer it) -> {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("humanPositionX[");
        _builder_1.append(it);
        _builder_1.append("], humanPositionY[");
        _builder_1.append(it);
        _builder_1.append("]");
        return _builder_1.toString();
      };
      Iterables.<String>addAll(posParts, IterableExtensions.<Integer, String>map(new IntegerRange(0, (hCount - 1)), _function_2));
    }
    if ((rCount > 0)) {
      final Function1<Integer, String> _function_3 = (Integer it) -> {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("robPositionX[");
        _builder_1.append(it);
        _builder_1.append("], robPositionY[");
        _builder_1.append(it);
        _builder_1.append("], batteryCharge[");
        _builder_1.append(it);
        _builder_1.append("]");
        return _builder_1.toString();
      };
      Iterables.<String>addAll(posParts, IterableExtensions.<Integer, String>map(new IntegerRange(0, (rCount - 1)), _function_3));
    }
    boolean _isEmpty = posParts.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("<query>");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("<formula>simulate[&lt;=");
      _builder_1.append(simTime, "\t");
      _builder_1.append("]{");
      String _join = IterableExtensions.join(posParts, ", ");
      _builder_1.append(_join, "\t");
      _builder_1.append(", ");
      _builder_1.append(patternName, "\t");
      _builder_1.append(".end}</formula>");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      _builder_1.append("<comment>Check the position of all the agent in the pattern for ");
      _builder_1.append(simTime, "\t");
      _builder_1.append(" time unit</comment>");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("</query>");
      _builder_1.newLine();
      queries.add(_builder_1.toString());
    }
    if (((rCount > 0) && (resCount > 0))) {
      final Function1<Integer, Iterable<String>> _function_4 = (Integer r) -> {
        final Function1<Integer, String> _function_5 = (Integer res) -> {
          StringConcatenation _builder_2 = new StringConcatenation();
          _builder_2.append("robot_inventory[");
          _builder_2.append(r);
          _builder_2.append("][");
          _builder_2.append(res);
          _builder_2.append("]");
          return _builder_2.toString();
        };
        return IterableExtensions.<Integer, String>map(new IntegerRange(0, (resCount - 1)), _function_5);
      };
      final String invs = IterableExtensions.join(IterableExtensions.<Integer, String>flatMap(new IntegerRange(0, (rCount - 1)), _function_4), ", ");
      StringConcatenation _builder_2 = new StringConcatenation();
      _builder_2.append("<query>");
      _builder_2.newLine();
      _builder_2.append("\t");
      _builder_2.append("<formula>simulate[&lt;=");
      _builder_2.append(simTime, "\t");
      _builder_2.append("]{");
      _builder_2.append(invs, "\t");
      _builder_2.append(", ");
      _builder_2.append(patternName, "\t");
      _builder_2.append(".end*2}</formula>");
      _builder_2.newLineIfNotEmpty();
      _builder_2.append("\t");
      _builder_2.append("<comment>Check quantity of each item for each robot</comment>");
      _builder_2.newLine();
      _builder_2.append("</query>");
      _builder_2.newLine();
      queries.add(_builder_2.toString());
    }
    boolean _isNullOrEmpty = IterableExtensions.isNullOrEmpty(pattern.getLocation());
    boolean _not_1 = (!_isNullOrEmpty);
    if (_not_1) {
      final Function1<Location, String> _function_5 = (Location it) -> {
        String _name = it.getName();
        String _plus = (_name + "[0], ");
        String _name_1 = it.getName();
        String _plus_1 = (_plus + _name_1);
        return (_plus_1 + "[1]");
      };
      final String locs = IterableExtensions.join(ListExtensions.<Location, String>map(pattern.getLocation(), _function_5), ", ");
      StringConcatenation _builder_3 = new StringConcatenation();
      _builder_3.append("<query>");
      _builder_3.newLine();
      _builder_3.append("\t");
      _builder_3.append("<formula>simulate[&lt;=");
      _builder_3.append(simTime, "\t");
      _builder_3.append("]{");
      _builder_3.append(locs, "\t");
      _builder_3.append("}</formula>");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append("\t");
      _builder_3.append("<comment>Plot the position of each location in the pattern</comment>");
      _builder_3.newLine();
      _builder_3.append("</query>");
      _builder_3.newLine();
      queries.add(_builder_3.toString());
    }
    if ((rCount > 0)) {
      final Function1<Integer, String> _function_6 = (Integer i) -> {
        StringConcatenation _builder_4 = new StringConcatenation();
        _builder_4.append("rm_");
        _builder_4.append(i);
        _builder_4.append(".idle, rf_");
        _builder_4.append(i);
        _builder_4.append(".idle, rp_");
        _builder_4.append(i);
        _builder_4.append(".idle, rd_");
        _builder_4.append(i);
        _builder_4.append(".idle");
        return _builder_4.toString();
      };
      final String rmodes = IterableExtensions.join(IterableExtensions.<Integer, String>map(new IntegerRange(1, rCount), _function_6), ", ");
      StringConcatenation _builder_4 = new StringConcatenation();
      _builder_4.append("<query>");
      _builder_4.newLine();
      _builder_4.append("\t");
      _builder_4.append("<formula>simulate[&lt;=");
      _builder_4.append(simTime, "\t");
      _builder_4.append("]{");
      _builder_4.append(rmodes, "\t");
      _builder_4.append("}</formula>");
      _builder_4.newLineIfNotEmpty();
      _builder_4.append("\t");
      _builder_4.append("<comment></comment>");
      _builder_4.newLine();
      _builder_4.append("</query>");
      _builder_4.newLine();
      queries.add(_builder_4.toString());
      final Function1<Integer, String> _function_7 = (Integer i) -> {
        StringConcatenation _builder_5 = new StringConcatenation();
        _builder_5.append("!rm_");
        _builder_5.append(i);
        _builder_5.append(".idle || !rf_");
        _builder_5.append(i);
        _builder_5.append(".idle || !rp_");
        _builder_5.append(i);
        _builder_5.append(".idle || !rd_");
        _builder_5.append(i);
        _builder_5.append(".idle");
        return _builder_5.toString();
      };
      final String rmodes2 = IterableExtensions.join(IterableExtensions.<Integer, String>map(new IntegerRange(1, rCount), _function_7), " || ");
      StringConcatenation _builder_5 = new StringConcatenation();
      _builder_5.append("<query>");
      _builder_5.newLine();
      _builder_5.append("\t");
      _builder_5.append("<formula>Pr[&lt;=");
      _builder_5.append(simTime, "\t");
      _builder_5.append("](&lt;&gt;");
      _builder_5.append(rmodes2, "\t");
      _builder_5.append(")</formula>");
      _builder_5.newLineIfNotEmpty();
      _builder_5.append("\t");
      _builder_5.append("<comment></comment>");
      _builder_5.newLine();
      _builder_5.append("</query>");
      _builder_5.newLine();
      queries.add(_builder_5.toString());
      final Function1<Integer, String> _function_8 = (Integer i) -> {
        StringConcatenation _builder_6 = new StringConcatenation();
        _builder_6.append("rm_");
        _builder_6.append(i);
        _builder_6.append(".idle &amp;&amp; rf_");
        _builder_6.append(i);
        _builder_6.append(".idle &amp;&amp; rp_");
        _builder_6.append(i);
        _builder_6.append(".idle &amp;&amp; rd_");
        _builder_6.append(i);
        _builder_6.append(".idle");
        return _builder_6.toString();
      };
      final String rmodes3 = IterableExtensions.join(IterableExtensions.<Integer, String>map(new IntegerRange(1, rCount), _function_8), " &amp;&amp; ");
      StringConcatenation _builder_6 = new StringConcatenation();
      _builder_6.append("<query>");
      _builder_6.newLine();
      _builder_6.append("\t");
      _builder_6.append("<formula>Pr[&lt;=");
      _builder_6.append(simTime, "\t");
      _builder_6.append("]([] (");
      _builder_6.append(patternName, "\t");
      _builder_6.append(".end imply (");
      _builder_6.append(rmodes3, "\t");
      _builder_6.append(")))</formula>");
      _builder_6.newLineIfNotEmpty();
      _builder_6.append("\t");
      _builder_6.append("<comment></comment>");
      _builder_6.newLine();
      _builder_6.append("</query>");
      _builder_6.newLine();
      queries.add(_builder_6.toString());
    }
    final Function1<Robot, String> _function_9 = (Robot it) -> {
      return it.getName();
    };
    final List<String> robotList = ListExtensions.<Robot, String>map(pattern.getRobot(), _function_9);
    final Function1<Human, String> _function_10 = (Human it) -> {
      return it.getName();
    };
    final List<String> humanList = ListExtensions.<Human, String>map(pattern.getHuman(), _function_10);
    EList<Sequence> _sequence_1 = model.getSequence();
    for (final Sequence seq_1 : _sequence_1) {
      {
        final int rIndex = robotList.indexOf(seq_1.getAgent().getName());
        final int hIndex = humanList.indexOf(seq_1.getAgent().getName());
        final boolean isRobot = (rIndex != (-1));
        final boolean isHuman = (hIndex != (-1));
        if ((isRobot || isHuman)) {
          final HashSet<String> targets = new HashSet<String>();
          EList<SubSequence> _subSequence = seq_1.getSubSequence();
          for (final SubSequence ss : _subSequence) {
            EList<ActionC> _actionC = ss.getActionC();
            for (final ActionC act : _actionC) {
              String _actionType = act.getActionType();
              boolean _equals = Objects.equals(_actionType, "moveTo");
              if (_equals) {
                targets.add(act.getTarget().getName());
              }
            }
          }
          String _xifexpression = null;
          if (isRobot) {
            _xifexpression = "robPositionX";
          } else {
            _xifexpression = "humanPositionX";
          }
          final String posX = _xifexpression;
          String _xifexpression_1 = null;
          if (isRobot) {
            _xifexpression_1 = "robPositionY";
          } else {
            _xifexpression_1 = "humanPositionY";
          }
          final String posY = _xifexpression_1;
          int _xifexpression_2 = (int) 0;
          if (isRobot) {
            _xifexpression_2 = rIndex;
          } else {
            _xifexpression_2 = hIndex;
          }
          final int idx = _xifexpression_2;
          String _xifexpression_3 = null;
          if (isRobot) {
            _xifexpression_3 = "robot";
          } else {
            _xifexpression_3 = "human";
          }
          final String typeName = _xifexpression_3;
          for (final String tName : targets) {
            StringConcatenation _builder_7 = new StringConcatenation();
            _builder_7.append("<query>");
            _builder_7.newLine();
            _builder_7.append("\t");
            _builder_7.append("<formula>Pr[&lt;=");
            _builder_7.append(simTime, "\t");
            _builder_7.append("] (&lt;&gt; pow(");
            _builder_7.append(posX, "\t");
            _builder_7.append("[");
            _builder_7.append(idx, "\t");
            _builder_7.append("] - ");
            _builder_7.append(tName, "\t");
            _builder_7.append("[0], 2) + pow(");
            _builder_7.append(posY, "\t");
            _builder_7.append("[");
            _builder_7.append(idx, "\t");
            _builder_7.append("] - ");
            _builder_7.append(tName, "\t");
            _builder_7.append("[1], 2) &lt;= 900 )</formula>");
            _builder_7.newLineIfNotEmpty();
            _builder_7.append("\t");
            _builder_7.append("<comment>Check if ");
            _builder_7.append(typeName, "\t");
            _builder_7.append(" ");
            String _name = seq_1.getAgent().getName();
            _builder_7.append(_name, "\t");
            _builder_7.append(" reaches ");
            _builder_7.append(tName, "\t");
            _builder_7.append(" (within 30 units)</comment>");
            _builder_7.newLineIfNotEmpty();
            _builder_7.append("</query>");
            _builder_7.newLine();
            queries.add(_builder_7.toString());
          }
        }
      }
    }
    final Procedure2<Human, Integer> _function_11 = (Human h, Integer i) -> {
      StringConcatenation _builder_7 = new StringConcatenation();
      _builder_7.append("<query>");
      _builder_7.newLine();
      _builder_7.append("\t");
      _builder_7.append("<formula>Pr[&lt;=");
      _builder_7.append(simTime, "\t");
      _builder_7.append("] (&lt;&gt; !isValidPosition(humanPositionX[");
      _builder_7.append(i, "\t");
      _builder_7.append("], humanPositionY[");
      _builder_7.append(i, "\t");
      _builder_7.append("]))</formula>");
      _builder_7.newLineIfNotEmpty();
      _builder_7.append("\t");
      _builder_7.append("<comment>Check if human ");
      String _name = h.getName();
      _builder_7.append(_name, "\t");
      _builder_7.append(" enters an invalid position (e.g. collision or obstacle)</comment>");
      _builder_7.newLineIfNotEmpty();
      _builder_7.append("</query>");
      _builder_7.newLine();
      queries.add(_builder_7.toString());
    };
    IterableExtensions.<Human>forEach(pattern.getHuman(), _function_11);
    final Procedure2<Robot, Integer> _function_12 = (Robot r, Integer i) -> {
      StringConcatenation _builder_7 = new StringConcatenation();
      _builder_7.append("<query>");
      _builder_7.newLine();
      _builder_7.append("\t");
      _builder_7.append("<formula>Pr[&lt;=");
      _builder_7.append(simTime, "\t");
      _builder_7.append("] (&lt;&gt; !isValidPosition(robPositionX[");
      _builder_7.append(i, "\t");
      _builder_7.append("], robPositionY[");
      _builder_7.append(i, "\t");
      _builder_7.append("]))</formula>");
      _builder_7.newLineIfNotEmpty();
      _builder_7.append("\t");
      _builder_7.append("<comment>Check if robot ");
      String _name = r.getName();
      _builder_7.append(_name, "\t");
      _builder_7.append(" enters an invalid position (e.g. collision or obstacle)</comment>");
      _builder_7.newLineIfNotEmpty();
      _builder_7.append("</query>");
      _builder_7.newLine();
      queries.add(_builder_7.toString());
    };
    IterableExtensions.<Robot>forEach(pattern.getRobot(), _function_12);
    StringConcatenation _builder_7 = new StringConcatenation();
    _builder_7.append("<query>");
    _builder_7.newLine();
    _builder_7.append("\t");
    _builder_7.append("<formula>Pr[&lt;=");
    _builder_7.append(simTime, "\t");
    _builder_7.append("] (&lt;&gt; currP > 2)</formula>");
    _builder_7.newLineIfNotEmpty();
    _builder_7.append("\t");
    _builder_7.append("<comment>Check if pattern is executed more then once</comment>");
    _builder_7.newLine();
    _builder_7.append("</query>");
    _builder_7.newLine();
    queries.add(_builder_7.toString());
    return IterableExtensions.join(queries, "\n");
  }
}
