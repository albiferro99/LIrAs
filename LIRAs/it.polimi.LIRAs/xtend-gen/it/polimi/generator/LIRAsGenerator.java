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
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.AbstractGenerator;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.generator.IGeneratorContext;
import org.eclipse.xtext.xbase.lib.ArrayExtensions;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@SuppressWarnings("all")
public class LIRAsGenerator extends AbstractGenerator {
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
      this.replace(filePath, "// **human_start_pos**", this.hInitPos(model), fsa);
      this.replace(filePath, "// **robot_initialization**", this.rInit(model), fsa);
      this.replace(filePath, "// **robot_start_pos**", this.rInitPos(model), fsa);
      this.hmoveTo(filePath, fsa, model);
      this.hidle(filePath, fsa, model);
      this.rmoveTo(filePath, fsa, model);
      this.rfollow(filePath, fsa, model);
      final int iNum = model.getPattern().getResources().size();
      if ((iNum != 0)) {
        this.rpick(filePath, fsa, model);
        this.rdeposit(filePath, fsa, model);
      }
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
    final int[] maxSSnum = { 0 };
    final StringBuilder builder = new StringBuilder();
    final Consumer<Sequence> _function = (Sequence sequence) -> {
      final Consumer<SubSequence> _function_1 = (SubSequence subSequence) -> {
        int _i = subSequence.getI();
        int _get = maxSSnum[0];
        boolean _greaterThan = (_i > _get);
        if (_greaterThan) {
          maxSSnum[0] = subSequence.getI();
        }
        int _j = subSequence.getJ();
        int _get_1 = maxSSnum[0];
        boolean _greaterThan_1 = (_j > _get_1);
        if (_greaterThan_1) {
          maxSSnum[0] = subSequence.getJ();
        }
      };
      sequence.getSubSequence().forEach(_function_1);
    };
    model.getSequence().forEach(_function);
    builder.append("urgent broadcast chan ");
    int _get = maxSSnum[0];
    IntegerRange _upTo = new IntegerRange(1, _get);
    for (final Integer i : _upTo) {
      {
        if (((i).intValue() > 1)) {
          builder.append(", ");
        }
        builder.append(String.format("s%d", i));
      }
    }
    builder.append(", e;");
    return builder.toString();
  }

  public String sStatus(final Model model) {
    final StringBuilder builder = new StringBuilder();
    final Consumer<Sequence> _function = (Sequence sequence) -> {
      builder.append(String.format("bool %s_stat = true;\n", sequence.getAgent().getName().toLowerCase()));
    };
    model.getSequence().forEach(_function);
    return builder.toString();
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
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("// No locations or resources declared. Using dummy to prevent compilation errors.");
      _builder.newLine();
      return _builder.toString();
    }
    final Function1<Location, String> _function = (Location it) -> {
      String _name = it.getName();
      return (_name + "[0]");
    };
    final List<String> locX = ListExtensions.<Location, String>map(locations, _function);
    final Function1<it.polimi.lIRAs.Resource, String> _function_1 = (it.polimi.lIRAs.Resource it) -> {
      String _name = it.getName();
      return (_name + "[0]");
    };
    final List<String> resX = ListExtensions.<it.polimi.lIRAs.Resource, String>map(resources, _function_1);
    final String stringX = IterableExtensions.join(Iterables.<String>concat(locX, resX), ", ");
    final Function1<Location, String> _function_2 = (Location it) -> {
      String _name = it.getName();
      return (_name + "[1]");
    };
    final List<String> locY = ListExtensions.<Location, String>map(locations, _function_2);
    final Function1<it.polimi.lIRAs.Resource, String> _function_3 = (it.polimi.lIRAs.Resource it) -> {
      String _name = it.getName();
      return (_name + "[1]");
    };
    final List<String> resY = ListExtensions.<it.polimi.lIRAs.Resource, String>map(resources, _function_3);
    final String stringY = IterableExtensions.join(Iterables.<String>concat(locY, resY), ", ");
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("double location_X[");
    _builder_1.append(totalSize);
    _builder_1.append("] = {");
    _builder_1.append(stringX);
    _builder_1.append("};");
    _builder_1.newLineIfNotEmpty();
    _builder_1.append("double location_Y[");
    _builder_1.append(totalSize);
    _builder_1.append("] = {");
    _builder_1.append(stringY);
    _builder_1.append("};");
    _builder_1.newLineIfNotEmpty();
    return _builder_1.toString();
  }

  public String iResource(final Model model) {
    final EList<it.polimi.lIRAs.Resource> resources = model.getPattern().getResources();
    boolean _isNullOrEmpty = IterableExtensions.isNullOrEmpty(resources);
    if (_isNullOrEmpty) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("// No resources in pattern");
      _builder.newLine();
      return _builder.toString();
    }
    final int size = resources.size();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("double resources_X[");
    _builder_1.append(size);
    _builder_1.append("] = {");
    final Function1<it.polimi.lIRAs.Resource, String> _function = (it.polimi.lIRAs.Resource it) -> {
      String _name = it.getName();
      return (_name + "[0]");
    };
    String _join = IterableExtensions.join(ListExtensions.<it.polimi.lIRAs.Resource, String>map(resources, _function), ", ");
    _builder_1.append(_join);
    _builder_1.append("};");
    _builder_1.newLineIfNotEmpty();
    _builder_1.append("double resources_Y[");
    _builder_1.append(size);
    _builder_1.append("] = {");
    final Function1<it.polimi.lIRAs.Resource, String> _function_1 = (it.polimi.lIRAs.Resource it) -> {
      String _name = it.getName();
      return (_name + "[1]");
    };
    String _join_1 = IterableExtensions.join(ListExtensions.<it.polimi.lIRAs.Resource, String>map(resources, _function_1), ", ");
    _builder_1.append(_join_1);
    _builder_1.append("};");
    _builder_1.newLineIfNotEmpty();
    return _builder_1.toString();
  }

  public String hNumber(final Model model) {
    final StringBuilder builder = new StringBuilder();
    final int hNum = model.getPattern().getHuman().size();
    if ((hNum != 0)) {
      builder.append(String.format("const int N_H = %d; // Number of humans in the pattern", Integer.valueOf(hNum)));
    } else {
      builder.append("// There are no Humans in the pattern Input");
    }
    return builder.toString();
  }

  public String rNumber(final Model model) {
    final StringBuilder builder = new StringBuilder();
    final int rNum = model.getPattern().getRobot().size();
    if ((rNum != 0)) {
      builder.append(String.format("const int N_R = %d; // Number of robots in the pattern", Integer.valueOf(rNum)));
    } else {
      builder.append("// There are no Robots in the pattern Input");
    }
    return builder.toString();
  }

  public String iNumber(final Model model) {
    final StringBuilder builder = new StringBuilder();
    final int iNum = model.getPattern().getResources().size();
    if ((iNum != 0)) {
      builder.append(String.format("const int N_ITEMS = %d; // Number of resources in the pattern", Integer.valueOf(iNum)));
    } else {
      builder.append("// There are no Resources in the pattern Input");
    }
    return builder.toString();
  }

  public String hInit(final Model model) {
    final StringBuilder builder = new StringBuilder();
    final int hNum = model.getPattern().getHuman().size();
    if ((hNum != 0)) {
      builder.append("// Human initialization\n");
      builder.append("double humanFatigue[N_H] = {");
      IntegerRange _upTo = new IntegerRange(1, hNum);
      for (final Integer i : _upTo) {
        {
          builder.append("0.0");
          if (((i).intValue() != hNum)) {
            builder.append(", ");
          }
        }
      }
      builder.append("};\n");
      builder.append("double humanPositionX[N_H] = {");
      IntegerRange _upTo_1 = new IntegerRange(1, hNum);
      for (final Integer i_1 : _upTo_1) {
        {
          builder.append("0.0");
          if (((i_1).intValue() != hNum)) {
            builder.append(", ");
          }
        }
      }
      builder.append("};\n");
      builder.append("double humanPositionY[N_H] = {");
      IntegerRange _upTo_2 = new IntegerRange(1, hNum);
      for (final Integer i_2 : _upTo_2) {
        {
          builder.append("0.0");
          if (((i_2).intValue() != hNum)) {
            builder.append(", ");
          }
        }
      }
      builder.append("};");
    }
    if ((hNum == 0)) {
      builder.append("double humanPositionX[1] = {0.0};\ndouble humanPositionY[1] = {0.0};");
    }
    return builder.toString();
  }

  public String hInitPos(final Model model) {
    final StringBuilder builder = new StringBuilder();
    final int hNum = model.getPattern().getHuman().size();
    if ((hNum != 0)) {
      builder.append("double h_startX[N_H] = {");
      IntegerRange _upTo = new IntegerRange(1, hNum);
      for (final Integer i : _upTo) {
        {
          builder.append("2500.0");
          if (((i).intValue() != hNum)) {
            builder.append(", ");
          }
        }
      }
      builder.append("};\n");
      builder.append("double h_startY[N_H] = {");
      IntegerRange _upTo_1 = new IntegerRange(1, hNum);
      for (final Integer i_1 : _upTo_1) {
        {
          builder.append("100.0");
          if (((i_1).intValue() != hNum)) {
            builder.append(", ");
          }
        }
      }
      builder.append("};");
    }
    return builder.toString();
  }

  public String rInit(final Model model) {
    final StringBuilder builder = new StringBuilder();
    final int rNum = model.getPattern().getRobot().size();
    if ((rNum != 0)) {
      builder.append("// Robot initialization\n");
      builder.append("double batteryCharge[N_R] = {");
      IntegerRange _upTo = new IntegerRange(1, rNum);
      for (final Integer i : _upTo) {
        {
          builder.append("100.0");
          if (((i).intValue() != rNum)) {
            builder.append(", ");
          }
        }
      }
      builder.append("};\n");
      builder.append("double robPositionX[N_R] = {");
      IntegerRange _upTo_1 = new IntegerRange(1, rNum);
      for (final Integer i_1 : _upTo_1) {
        {
          builder.append("0.0");
          if (((i_1).intValue() != rNum)) {
            builder.append(", ");
          }
        }
      }
      builder.append("};\n");
      builder.append("double robPositionY[N_R] = {");
      IntegerRange _upTo_2 = new IntegerRange(1, rNum);
      for (final Integer i_2 : _upTo_2) {
        {
          builder.append("0.0");
          if (((i_2).intValue() != rNum)) {
            builder.append(", ");
          }
        }
      }
      builder.append("};\n");
    } else {
      builder.append("// None input Robot has been declared");
    }
    return builder.toString();
  }

  public String rInitPos(final Model model) {
    final StringBuilder builder = new StringBuilder();
    final int rNum = model.getPattern().getRobot().size();
    if ((rNum != 0)) {
      builder.append("double r_startX[N_R] = {");
      IntegerRange _upTo = new IntegerRange(1, rNum);
      for (final Integer i : _upTo) {
        {
          builder.append("2000.0");
          if (((i).intValue() != rNum)) {
            builder.append(", ");
          }
        }
      }
      builder.append("};\n");
      builder.append("double r_startY[N_R] = {");
      IntegerRange _upTo_1 = new IntegerRange(1, rNum);
      for (final Integer i_1 : _upTo_1) {
        {
          builder.append("500.0");
          if (((i_1).intValue() != rNum)) {
            builder.append(", ");
          }
        }
      }
      builder.append("};");
    }
    return builder.toString();
  }

  public void hidle(final String filePath, final IFileSystemAccess2 fsa, final Model model) {
    final int hNum = model.getPattern().getHuman().size();
    if ((hNum != 0)) {
      this.replace(filePath, "// **h_idle_sync**", "urgent broadcast chan obey_h_moveTo[N_H];", fsa);
      this.replace(filePath, "// **Human_idle**", this.readHidle(), fsa);
      this.replace(filePath, "// **missDef_hidle**", this.missDef_hidle(model), fsa);
      this.replace(filePath, "// **missDec_hidle**", this.missDec_hidle(model), fsa);
    }
  }

  public String readHidle() {
    final String filePath = "/Users/alberto_tagliaferro/Library/CloudStorage/OneDrive-PolitecnicodiMilano/Desktop/LIRAs_Generation/runtime-EclipseApplication/src-gen/Human_idle.rtf";
    try {
      final FileInputStream fis = new FileInputStream(filePath);
      final RTFEditorKit rtfKit = new RTFEditorKit();
      final Document doc = rtfKit.createDefaultDocument();
      rtfKit.read(fis, doc, 0);
      final String text = doc.getText(0, doc.getLength());
      fis.close();
      return text;
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception e = (Exception)_t;
        e.printStackTrace();
        return "Error reading file";
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }

  public String missDef_hidle(final Model model) {
    final StringBuilder builder = new StringBuilder();
    final int hNum = model.getPattern().getHuman().size();
    if ((hNum != 0)) {
      IntegerRange _upTo = new IntegerRange(1, hNum);
      for (final Integer i : _upTo) {
        {
          builder.append(String.format("hi_%d = Human_idle(%d);", i, i));
          if (((i).intValue() != hNum)) {
            builder.append("\n");
          }
        }
      }
    }
    return builder.toString();
  }

  public String missDec_hidle(final Model model) {
    final StringBuilder builder = new StringBuilder();
    final int hNum = model.getPattern().getHuman().size();
    if ((hNum != 0)) {
      IntegerRange _upTo = new IntegerRange(1, hNum);
      for (final Integer i : _upTo) {
        {
          builder.append(String.format("hi_%d,", i));
          if (((i).intValue() != hNum)) {
            builder.append("\n");
          }
        }
      }
    }
    return builder.toString();
  }

  public void hmoveTo(final String filePath, final IFileSystemAccess2 fsa, final Model model) {
    final int hNum = model.getPattern().getHuman().size();
    if ((hNum != 0)) {
      this.replace(filePath, "// **h_moveTo_sync**", "urgent broadcast chan start_h_moveTo[N_H], stop_h_moveTo[N_H];", fsa);
      this.replace(filePath, "// **h_moveTo_var**", "int human_loc_target[N_H];", fsa);
      this.replace(filePath, "// **h_moveTo_chan**", "broadcast chan hm_fail;", fsa);
      final int lNum = model.getPattern().getLocation().size();
      final int resNum = model.getPattern().getResources().size();
      if (((lNum != 0) || (resNum != 0))) {
        this.replace(filePath, "// **Human_moveTo**", this.readHmoveTo(), fsa);
        this.replace(filePath, "// **missDef_hm**", this.missDef_hm(model), fsa);
        this.replace(filePath, "// **missDec_hm**", this.missDec_hm(model), fsa);
      }
    }
  }

  public String readHmoveTo() {
    final String filePath = "/Users/alberto_tagliaferro/Library/CloudStorage/OneDrive-PolitecnicodiMilano/Desktop/LIRAs_Generation/runtime-EclipseApplication/src-gen/Human_moveTo.rtf";
    try {
      final FileInputStream fis = new FileInputStream(filePath);
      final RTFEditorKit rtfKit = new RTFEditorKit();
      final Document doc = rtfKit.createDefaultDocument();
      rtfKit.read(fis, doc, 0);
      final String text = doc.getText(0, doc.getLength());
      fis.close();
      return text;
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception e = (Exception)_t;
        e.printStackTrace();
        return "Error reading file";
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }

  public String missDef_hm(final Model model) {
    final StringBuilder builder = new StringBuilder();
    final int hNum = model.getPattern().getHuman().size();
    if ((hNum != 0)) {
      IntegerRange _upTo = new IntegerRange(1, hNum);
      for (final Integer i : _upTo) {
        {
          builder.append(String.format("hm_%d = Human_moveTo(%d, 26.0);", i, i));
          if (((i).intValue() != hNum)) {
            builder.append("\n");
          }
        }
      }
    }
    return builder.toString();
  }

  public String missDec_hm(final Model model) {
    final StringBuilder builder = new StringBuilder();
    final int hNum = model.getPattern().getHuman().size();
    if ((hNum != 0)) {
      IntegerRange _upTo = new IntegerRange(1, hNum);
      for (final Integer i : _upTo) {
        {
          builder.append(String.format("hm_%d,", i));
          if (((i).intValue() != hNum)) {
            builder.append("\n");
          }
        }
      }
    }
    return builder.toString();
  }

  public void rmoveTo(final String filePath, final IFileSystemAccess2 fsa, final Model model) {
    final int rNum = model.getPattern().getRobot().size();
    if ((rNum != 0)) {
      this.replace(filePath, "// **r_moveTo_sync**", "urgent broadcast chan start_r_moveTo[N_R], stop_r_moveTo[N_R];", fsa);
      this.replace(filePath, "// **r_moveTo_var**", "int robot_loc_target[N_R];", fsa);
      this.replace(filePath, "// **r_moveTo_chan**", "broadcast chan rm_still;", fsa);
      final int lNum = model.getPattern().getLocation().size();
      final int resNum = model.getPattern().getResources().size();
      if (((lNum != 0) || (resNum != 0))) {
        this.replace(filePath, "// **Robot_moveTo**", this.readRmoveTo(), fsa);
        this.replace(filePath, "// **missDef_rm**", this.missDef_rm(model), fsa);
        this.replace(filePath, "// **missDec_rm**", this.missDec_rm(model), fsa);
      }
    }
  }

  public String readRmoveTo() {
    final String filePath = "/Users/alberto_tagliaferro/Library/CloudStorage/OneDrive-PolitecnicodiMilano/Desktop/LIRAs_Generation/runtime-EclipseApplication/src-gen/Robot_moveTo.rtf";
    try {
      final FileInputStream fis = new FileInputStream(filePath);
      final RTFEditorKit rtfKit = new RTFEditorKit();
      final Document doc = rtfKit.createDefaultDocument();
      rtfKit.read(fis, doc, 0);
      final String text = doc.getText(0, doc.getLength());
      fis.close();
      return text;
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception e = (Exception)_t;
        e.printStackTrace();
        return "Error reading file";
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }

  public String missDef_rm(final Model model) {
    final StringBuilder builder = new StringBuilder();
    final int rNum = model.getPattern().getRobot().size();
    IntegerRange _upTo = new IntegerRange(1, rNum);
    for (final Integer i : _upTo) {
      {
        builder.append(String.format("rm_%d = Robot_moveTo(%d, 26.0, 26.0);", i, i));
        if (((i).intValue() != rNum)) {
          builder.append("\n");
        }
      }
    }
    return builder.toString();
  }

  public String missDec_rm(final Model model) {
    final StringBuilder builder = new StringBuilder();
    final int rNum = model.getPattern().getRobot().size();
    IntegerRange _upTo = new IntegerRange(1, rNum);
    for (final Integer i : _upTo) {
      {
        builder.append(String.format("rm_%d,", i));
        if (((i).intValue() != rNum)) {
          builder.append("\n");
        }
      }
    }
    return builder.toString();
  }

  public void rfollow(final String filePath, final IFileSystemAccess2 fsa, final Model model) {
    final int rNum = model.getPattern().getRobot().size();
    if ((rNum != 0)) {
      this.replace(filePath, "// **r_follow_sync**", "urgent broadcast chan start_r_follow[N_R], stop_r_follow[N_R];", fsa);
      this.replace(filePath, "// **r_follow_var**", "// Who I have to follow (agent id)?\nint human_to_be_followed[N_R];\nint robot_to_be_followed[N_R];\nint f_loc_target[N_R];", fsa);
      this.replace(filePath, "// **r_follow_chan**", "broadcast chan rf_still;", fsa);
      this.replace(filePath, "// **Robot_follow**", this.readRfollow(), fsa);
      this.replace(filePath, "// **missDef_rf**", this.missDef_rf(model), fsa);
      this.replace(filePath, "// **missDec_rf**", this.missDec_rf(model), fsa);
    }
  }

  public String readRfollow() {
    final String filePath = "/Users/alberto_tagliaferro/Library/CloudStorage/OneDrive-PolitecnicodiMilano/Desktop/LIRAs_Generation/runtime-EclipseApplication/src-gen/Robot_follow.rtf";
    try {
      final FileInputStream fis = new FileInputStream(filePath);
      final RTFEditorKit rtfKit = new RTFEditorKit();
      final Document doc = rtfKit.createDefaultDocument();
      rtfKit.read(fis, doc, 0);
      final String text = doc.getText(0, doc.getLength());
      fis.close();
      return text;
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception e = (Exception)_t;
        e.printStackTrace();
        return "Error reading file";
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }

  public String missDef_rf(final Model model) {
    final StringBuilder builder = new StringBuilder();
    final int rNum = model.getPattern().getRobot().size();
    IntegerRange _upTo = new IntegerRange(1, rNum);
    for (final Integer i : _upTo) {
      {
        builder.append(String.format("rf_%d = Robot_follow(%d, 26.0, 26.0);", i, i));
        if (((i).intValue() != rNum)) {
          builder.append("\n");
        }
      }
    }
    return builder.toString();
  }

  public String missDec_rf(final Model model) {
    final StringBuilder builder = new StringBuilder();
    final int rNum = model.getPattern().getRobot().size();
    IntegerRange _upTo = new IntegerRange(1, rNum);
    for (final Integer i : _upTo) {
      {
        builder.append(String.format("rf_%d,", i));
        if (((i).intValue() != rNum)) {
          builder.append("\n");
        }
      }
    }
    return builder.toString();
  }

  public void rpick(final String filePath, final IFileSystemAccess2 fsa, final Model model) {
    final int rNum = model.getPattern().getRobot().size();
    final int iNum = model.getPattern().getResources().size();
    if ((rNum != 0)) {
      this.replace(filePath, "// **r_pick_sync**", "urgent broadcast chan start_r_pick[N_R], stop_r_pick[N_R];", fsa);
      String inventoryVars = null;
      if ((iNum != 0)) {
        inventoryVars = "int robot_res_to_pick[N_R];\nint robot_inventory[N_R][N_ITEMS];\nint old_inventory[N_R][N_ITEMS];";
      } else {
        inventoryVars = "// No resources defined, inventory variables skipped";
      }
      this.replace(filePath, "// **r_pick_var**", inventoryVars, fsa);
      this.replace(filePath, "// **Robot_pick**", this.readRpick(), fsa);
      this.replace(filePath, "// **missDef_rp**", this.missDef_rp(model), fsa);
      this.replace(filePath, "// **missDec_rp**", this.missDec_rp(model), fsa);
    }
  }

  public String readRpick() {
    final String filePath = "/Users/alberto_tagliaferro/Library/CloudStorage/OneDrive-PolitecnicodiMilano/Desktop/LIRAs_Generation/runtime-EclipseApplication/src-gen/Robot_pick.rtf";
    try {
      final FileInputStream fis = new FileInputStream(filePath);
      final RTFEditorKit rtfKit = new RTFEditorKit();
      final Document doc = rtfKit.createDefaultDocument();
      rtfKit.read(fis, doc, 0);
      final String text = doc.getText(0, doc.getLength());
      fis.close();
      return text;
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception e = (Exception)_t;
        e.printStackTrace();
        return "Error reading Robot_pick.rtf file";
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }

  public String missDef_rp(final Model model) {
    final StringBuilder builder = new StringBuilder();
    final int rNum = model.getPattern().getRobot().size();
    IntegerRange _upTo = new IntegerRange(1, rNum);
    for (final Integer i : _upTo) {
      {
        builder.append(String.format("rp_%d = Robot_pick(%d);", i, i));
        if (((i).intValue() != rNum)) {
          builder.append("\n");
        }
      }
    }
    return builder.toString();
  }

  public String missDec_rp(final Model model) {
    final StringBuilder builder = new StringBuilder();
    final int rNum = model.getPattern().getRobot().size();
    IntegerRange _upTo = new IntegerRange(1, rNum);
    for (final Integer i : _upTo) {
      {
        builder.append(String.format("rp_%d,", i));
        if (((i).intValue() != rNum)) {
          builder.append("\n");
        }
      }
    }
    return builder.toString();
  }

  public void rdeposit(final String filePath, final IFileSystemAccess2 fsa, final Model model) {
    final int rNum = model.getPattern().getRobot().size();
    if ((rNum != 0)) {
      this.replace(filePath, "// **r_deposit_sync**", "urgent broadcast chan start_r_deposit[N_R], stop_r_deposit[N_R];", fsa);
      this.replace(filePath, "// **r_deposit_var**", "int robot_res_to_deposit[N_R];", fsa);
      this.replace(filePath, "// **Robot_deposit**", this.readRdeposit(), fsa);
      this.replace(filePath, "// **missDef_rd**", this.missDef_rd(model), fsa);
      this.replace(filePath, "// **missDec_rd**", this.missDec_rd(model), fsa);
    }
  }

  public String readRdeposit() {
    final String filePath = "/Users/alberto_tagliaferro/Library/CloudStorage/OneDrive-PolitecnicodiMilano/Desktop/LIRAs_Generation/runtime-EclipseApplication/src-gen/Robot_deposit.rtf";
    try {
      final FileInputStream fis = new FileInputStream(filePath);
      final RTFEditorKit rtfKit = new RTFEditorKit();
      final Document doc = rtfKit.createDefaultDocument();
      rtfKit.read(fis, doc, 0);
      final String text = doc.getText(0, doc.getLength());
      fis.close();
      return text;
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception e = (Exception)_t;
        e.printStackTrace();
        return "Error reading Robot_deposit.rtf file";
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }

  public String missDef_rd(final Model model) {
    final StringBuilder builder = new StringBuilder();
    final int rNum = model.getPattern().getRobot().size();
    IntegerRange _upTo = new IntegerRange(1, rNum);
    for (final Integer i : _upTo) {
      {
        builder.append(String.format("rd_%d = Robot_deposit(%d);", i, i));
        if (((i).intValue() != rNum)) {
          builder.append("\n");
        }
      }
    }
    return builder.toString();
  }

  public String missDec_rd(final Model model) {
    final StringBuilder builder = new StringBuilder();
    final int rNum = model.getPattern().getRobot().size();
    IntegerRange _upTo = new IntegerRange(1, rNum);
    for (final Integer i : _upTo) {
      {
        builder.append(String.format("rd_%d,", i));
        if (((i).intValue() != rNum)) {
          builder.append("\n");
        }
      }
    }
    return builder.toString();
  }

  public String pattDef(final Model model) {
    final StringBuilder builder = new StringBuilder();
    final int[] rNum = { 1 };
    final int[] hNum = { 1 };
    builder.append(String.format("%s = %s_orchestrator(1);\n", model.getPattern().getName().toString(), model.getPattern().getName().toString()));
    final Consumer<Sequence> _function = (Sequence sequence) -> {
      final Function1<Human, String> _function_1 = (Human it) -> {
        return it.getName();
      };
      boolean _contains = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_1).contains(sequence.getAgent().getName());
      if (_contains) {
        builder.append(String.format("%s_%s_def = %s_%s(1, %d);\n", model.getPattern().getName().toString(), sequence.getAgent().getName().toLowerCase(), model.getPattern().getName().toString(), sequence.getAgent().getName().toLowerCase(), Integer.valueOf(hNum[0])));
        int _get = hNum[0];
        int _plus = (_get + 1);
        hNum[0] = _plus;
      }
      final Function1<Robot, String> _function_2 = (Robot it) -> {
        return it.getName();
      };
      boolean _contains_1 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_2).contains(sequence.getAgent().getName());
      if (_contains_1) {
        builder.append(String.format("%s_%s_def = %s_%s(1, %d);\n", model.getPattern().getName().toString(), sequence.getAgent().getName().toLowerCase(), model.getPattern().getName().toString(), sequence.getAgent().getName().toLowerCase(), Integer.valueOf(rNum[0])));
        int _get_1 = rNum[0];
        int _plus_1 = (_get_1 + 1);
        rNum[0] = _plus_1;
      }
    };
    model.getSequence().forEach(_function);
    return builder.toString();
  }

  public String pattDic(final Model model) {
    final StringBuilder builder = new StringBuilder();
    builder.append(String.format("%s,\n", model.getPattern().getName().toString(), model.getPattern().getName().toString()));
    final Consumer<Sequence> _function = (Sequence sequence) -> {
      final Function1<Human, String> _function_1 = (Human it) -> {
        return it.getName();
      };
      boolean _contains = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_1).contains(sequence.getAgent().getName());
      if (_contains) {
        builder.append(String.format("%s_%s_def,\n", model.getPattern().getName().toString(), sequence.getAgent().getName().toLowerCase()));
      }
      final Function1<Robot, String> _function_2 = (Robot it) -> {
        return it.getName();
      };
      boolean _contains_1 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_2).contains(sequence.getAgent().getName());
      if (_contains_1) {
        builder.append(String.format("%s_%s_def,\n", model.getPattern().getName().toString(), sequence.getAgent().getName().toLowerCase()));
      }
    };
    model.getSequence().forEach(_function);
    int _length = builder.length();
    boolean _greaterThan = (_length > 0);
    if (_greaterThan) {
      int _length_1 = builder.length();
      int _minus = (_length_1 - 2);
      builder.deleteCharAt(_minus);
    }
    return builder.toString();
  }

  public String pattOrch(final Model model) {
    final StringBuilder builder = new StringBuilder();
    final StringBuilder guard = new StringBuilder();
    final StringBuilder update = new StringBuilder();
    final String patternName = model.getPattern().getName().toString();
    final int[] id = { 0 };
    final int[] seqNum = { 0 };
    final int[] x = { 0 };
    final int[] y = { 0 };
    final int[] maxIndexHelper = { 0 };
    builder.append("\t<template>\n");
    builder.append(String.format("\t\t<name>%s_orchestrator</name>\n", patternName));
    builder.append(String.format("\t\t<parameter>int p</parameter>\n"));
    builder.append("\t\t<declaration>// Place local declarations here.\n");
    builder.append("\t\t</declaration>\n");
    final Consumer<Sequence> _function = (Sequence sequence) -> {
      final Consumer<SubSequence> _function_1 = (SubSequence subSequence) -> {
        int _i = subSequence.getI();
        int _get = maxIndexHelper[0];
        boolean _greaterThan = (_i > _get);
        if (_greaterThan) {
          maxIndexHelper[0] = subSequence.getI();
        }
        int _j = subSequence.getJ();
        int _get_1 = maxIndexHelper[0];
        boolean _greaterThan_1 = (_j > _get_1);
        if (_greaterThan_1) {
          maxIndexHelper[0] = subSequence.getJ();
        }
      };
      sequence.getSubSequence().forEach(_function_1);
    };
    model.getSequence().forEach(_function);
    builder.append(this.genLoc(id[0], x[0], y[0], "Init", null, null, false, false));
    int _get = id[0];
    int _plus = (_get + 1);
    id[0] = _plus;
    int _get_1 = maxIndexHelper[0];
    int _plus_1 = (_get_1 + 1);
    IntegerRange _upTo = new IntegerRange(1, _plus_1);
    for (final Integer i : _upTo) {
      int _get_2 = maxIndexHelper[0];
      boolean _lessEqualsThan = ((i).intValue() <= _get_2);
      if (_lessEqualsThan) {
        int _get_3 = id[0];
        int _multiply = (_get_3 * 500);
        int _get_4 = id[0];
        int _multiply_1 = (_get_4 * 0);
        builder.append(this.genLoc(id[0], _multiply, _multiply_1, String.format("sub_seq_s%d", Integer.valueOf(id[0])), null, null, false, false));
        int _get_5 = id[0];
        int _plus_2 = (_get_5 + 1);
        id[0] = _plus_2;
      } else {
        int _get_6 = id[0];
        int _multiply_2 = (_get_6 * 500);
        int _get_7 = id[0];
        int _multiply_3 = (_get_7 * 0);
        builder.append(this.genLoc(id[0], _multiply_2, _multiply_3, "end", null, null, false, false));
        int _get_8 = id[0];
        int _plus_3 = (_get_8 + 1);
        id[0] = _plus_3;
      }
    }
    final Consumer<Sequence> _function_1 = (Sequence sequence) -> {
      int _get_9 = seqNum[0];
      int _plus_4 = (_get_9 + 1);
      seqNum[0] = _plus_4;
    };
    model.getSequence().forEach(_function_1);
    builder.append(String.format("\t\t<init ref=\"id%d\"/>\n", Integer.valueOf(0)));
    int _get_9 = seqNum[0];
    int _get_10 = maxIndexHelper[0];
    int _multiply_4 = (_get_9 * _get_10);
    final int[] continuation = new int[_multiply_4];
    final int[] s = { 0 };
    final int[] ss = { 0 };
    final Consumer<Sequence> _function_2 = (Sequence sequence) -> {
      final Consumer<SubSequence> _function_3 = (SubSequence subSequence) -> {
        int _j = subSequence.getJ();
        int _i = subSequence.getI();
        int _minus = (_j - _i);
        boolean _greaterThan = (_minus > 0);
        if (_greaterThan) {
          int _i_1 = subSequence.getI();
          int _plus_4 = (_i_1 + 1);
          int _j_1 = subSequence.getJ();
          IntegerRange _upTo_1 = new IntegerRange(_plus_4, _j_1);
          for (final Integer j : _upTo_1) {
            int _get_11 = seqNum[0];
            int _multiply_5 = (((j).intValue() - 1) * _get_11);
            int _get_12 = s[0];
            int _plus_5 = (_multiply_5 + _get_12);
            continuation[_plus_5] = 1;
          }
        }
        int _get_13 = s[0];
        int _plus_6 = (_get_13 + 1);
        ss[0] = _plus_6;
      };
      sequence.getSubSequence().forEach(_function_3);
      int _get_11 = s[0];
      int _plus_4 = (_get_11 + 1);
      s[0] = _plus_4;
    };
    model.getSequence().forEach(_function_2);
    s[0] = 0;
    ss[0] = 0;
    guard.setLength(0);
    int _get_11 = maxIndexHelper[0];
    int _minus = (_get_11 - 1);
    IntegerRange _upTo_1 = new IntegerRange(0, _minus);
    for (final Integer i_1 : _upTo_1) {
      {
        int _get_12 = seqNum[0];
        int _minus_1 = (_get_12 - 1);
        IntegerRange _upTo_2 = new IntegerRange(0, _minus_1);
        for (final Integer j : _upTo_2) {
          int _get_13 = seqNum[0];
          int _multiply_5 = ((i_1).intValue() * _get_13);
          int _plus_4 = (_multiply_5 + (j).intValue());
          int _get_14 = continuation[_plus_4];
          boolean _equals = (_get_14 == 0);
          if (_equals) {
            final Consumer<Sequence> _function_3 = (Sequence sequence) -> {
              if ((((j).intValue() == s[0]) && Objects.equals(guard.toString(), ""))) {
                guard.append(sequence.getAgent().getName().toLowerCase());
                guard.append("_stat==true");
                update.append(sequence.getAgent().getName().toLowerCase());
                update.append("_stat=false");
              } else {
                int _get_15 = s[0];
                boolean _equals_1 = ((j).intValue() == _get_15);
                if (_equals_1) {
                  guard.append(" and ");
                  guard.append(sequence.getAgent().getName().toLowerCase());
                  guard.append("_stat==true");
                  update.append(", ");
                  update.append(sequence.getAgent().getName().toLowerCase());
                  update.append("_stat=false");
                }
              }
              int _get_16 = s[0];
              int _plus_5 = (_get_16 + 1);
              s[0] = _plus_5;
            };
            model.getSequence().forEach(_function_3);
            s[0] = 0;
          }
        }
        if (((i_1).intValue() == 0)) {
          guard.append(" and p==currP");
        }
        builder.append(this.genTran(id[0], (i_1).intValue(), ((i_1).intValue() + 1), null, guard.toString(), String.format("s%d!", Integer.valueOf(((i_1).intValue() + 1))), update.toString(), true, 500, 0, 0, 0));
        int _get_15 = id[0];
        int _plus_5 = (_get_15 + 1);
        id[0] = _plus_5;
        guard.setLength(0);
        update.setLength(0);
        int _get_16 = maxIndexHelper[0];
        int _minus_2 = (_get_16 - 1);
        boolean _equals_1 = ((i_1).intValue() == _minus_2);
        if (_equals_1) {
          final Consumer<Sequence> _function_4 = (Sequence sequence) -> {
            String _string = guard.toString();
            boolean _equals_2 = Objects.equals(_string, "");
            if (_equals_2) {
              guard.append(sequence.getAgent().getName().toLowerCase());
              guard.append("_stat==true");
            } else {
              guard.append(" and ");
              guard.append(sequence.getAgent().getName().toLowerCase());
              guard.append("_stat==true");
            }
            int _get_17 = s[0];
            int _plus_6 = (_get_17 + 1);
            s[0] = _plus_6;
          };
          model.getSequence().forEach(_function_4);
          update.append("currP=currP+1");
          builder.append(this.genTran(id[0], ((i_1).intValue() + 1), ((i_1).intValue() + 2), null, guard.toString(), String.format("e!"), update.toString(), true, 500, 0, 0, 0));
          int _get_17 = id[0];
          int _plus_6 = (_get_17 + 1);
          id[0] = _plus_6;
          guard.setLength(0);
          update.setLength(0);
        }
      }
    }
    builder.append("\t</template>\n");
    return builder.toString();
  }

  public String seqOrch(final Model model) {
    final StringBuilder transition = new StringBuilder();
    final StringBuilder builder = new StringBuilder();
    final StringBuilder guard = new StringBuilder();
    final StringBuilder update = new StringBuilder();
    final StringBuilder sync = new StringBuilder();
    final int[] id = { 0 };
    final int[] AgentID = { 0 };
    final int[] actionID = { 0 };
    final int[] locNum = { 0 };
    final int[] resNum = { 0 };
    final int[] x = { 0 };
    final int[] y = { 0 };
    final int[] maxIndex = { 0 };
    final int[] endID = { 0 };
    final int xOffset = 200;
    final int yOffset = 350;
    final int[] actionCount = { 0 };
    final Consumer<Sequence> _function = (Sequence sequence) -> {
      final Consumer<SubSequence> _function_1 = (SubSequence subSequence) -> {
        int _i = subSequence.getI();
        int _get = maxIndex[0];
        boolean _greaterThan = (_i > _get);
        if (_greaterThan) {
          maxIndex[0] = subSequence.getI();
        }
        int _j = subSequence.getJ();
        int _get_1 = maxIndex[0];
        boolean _greaterThan_1 = (_j > _get_1);
        if (_greaterThan_1) {
          maxIndex[0] = subSequence.getJ();
        }
      };
      sequence.getSubSequence().forEach(_function_1);
    };
    model.getSequence().forEach(_function);
    final Consumer<Location> _function_1 = (Location loc) -> {
      int _get = locNum[0];
      int _plus = (_get + 1);
      locNum[0] = _plus;
    };
    model.getPattern().getLocation().forEach(_function_1);
    final String[] locations = new String[locNum[0]];
    locNum[0] = 0;
    final Consumer<Location> _function_2 = (Location loc) -> {
      locations[locNum[0]] = loc.getName().toString();
      int _get = locNum[0];
      int _plus = (_get + 1);
      locNum[0] = _plus;
    };
    model.getPattern().getLocation().forEach(_function_2);
    final Consumer<it.polimi.lIRAs.Resource> _function_3 = (it.polimi.lIRAs.Resource res) -> {
      int _get = resNum[0];
      int _plus = (_get + 1);
      resNum[0] = _plus;
    };
    model.getPattern().getResources().forEach(_function_3);
    final String[] resources = new String[resNum[0]];
    resNum[0] = 0;
    final Consumer<it.polimi.lIRAs.Resource> _function_4 = (it.polimi.lIRAs.Resource res) -> {
      resources[resNum[0]] = res.getName().toString();
      int _get = resNum[0];
      int _plus = (_get + 1);
      resNum[0] = _plus;
    };
    model.getPattern().getResources().forEach(_function_4);
    final int[] continuation = new int[maxIndex[0]];
    final int[] emptySS = new int[maxIndex[0]];
    int _get = maxIndex[0];
    int _plus = (_get + 1);
    final int[] firstActionID = new int[_plus];
    int _get_1 = maxIndex[0];
    int _plus_1 = (_get_1 + 1);
    final int[] lastActionID = new int[_plus_1];
    int _get_2 = maxIndex[0];
    int _plus_2 = (_get_2 + 1);
    final int[] endSubSeqID = new int[_plus_2];
    final Consumer<Sequence> _function_5 = (Sequence sequence) -> {
      id[0] = 0;
      x[0] = 0;
      y[0] = 0;
      int _get_3 = AgentID[0];
      int _plus_3 = (_get_3 + 1);
      AgentID[0] = _plus_3;
      actionID[0] = 1;
      final Consumer<SubSequence> _function_6 = (SubSequence subSequence) -> {
        int _j = subSequence.getJ();
        int _i = subSequence.getI();
        int _minus = (_j - _i);
        boolean _greaterThan = (_minus > 0);
        if (_greaterThan) {
          int _i_1 = subSequence.getI();
          int _plus_4 = (_i_1 + 1);
          int _j_1 = subSequence.getJ();
          IntegerRange _upTo = new IntegerRange(_plus_4, _j_1);
          for (final Integer j : _upTo) {
            continuation[((j).intValue() - 1)] = 1;
          }
        }
      };
      sequence.getSubSequence().forEach(_function_6);
      builder.append("\t<template>\n");
      builder.append(String.format("\t\t<name>%s_%s</name>\n", model.getPattern().getName(), sequence.getAgent().getName().toLowerCase()));
      final Function1<Human, String> _function_7 = (Human it) -> {
        return it.getName();
      };
      boolean _contains = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_7).contains(sequence.getAgent().getName());
      if (_contains) {
        builder.append(String.format("\t\t<parameter>int p, int H</parameter>\n"));
      }
      final Function1<Robot, String> _function_8 = (Robot it) -> {
        return it.getName();
      };
      boolean _contains_1 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_8).contains(sequence.getAgent().getName());
      if (_contains_1) {
        builder.append(String.format("\t\t<parameter>int p, int R</parameter>\n"));
      }
      builder.append("\t\t<declaration>// Place local declarations here.\nclock t;");
      builder.append("\t\t</declaration>\n");
      builder.append(this.genLoc(id[0], x[0], y[0], String.format("init_%s", sequence.getAgent().getName().toLowerCase()), null, null, false, false));
      int _get_4 = id[0];
      int _plus_4 = (_get_4 + 1);
      id[0] = _plus_4;
      int _get_5 = maxIndex[0];
      IntegerRange _upTo = new IntegerRange(1, _get_5);
      for (final Integer i : _upTo) {
        {
          int _get_6 = y[0];
          int _plus_5 = (_get_6 + yOffset);
          y[0] = _plus_5;
          x[0] = 0;
          int _get_7 = continuation[((i).intValue() - 1)];
          boolean _equals = (_get_7 == 0);
          if (_equals) {
            builder.append(this.genLoc(id[0], x[0], y[0], String.format("s%d_start", i), null, null, false, true));
          }
          int _get_8 = id[0];
          int _plus_6 = (_get_8 + 1);
          id[0] = _plus_6;
          continuation[((i).intValue() - 1)] = 0;
          int _get_9 = maxIndex[0];
          boolean _equals_1 = ((i).intValue() == _get_9);
          if (_equals_1) {
            int _get_10 = y[0];
            int _plus_7 = (_get_10 + yOffset);
            y[0] = _plus_7;
            x[0] = 0;
            builder.append(this.genLoc(id[0], x[0], y[0], String.format("end_%s", sequence.getAgent().getName().toLowerCase()), null, null, false, false));
            endID[0] = id[0];
            int _get_11 = id[0];
            int _plus_8 = (_get_11 + 1);
            id[0] = _plus_8;
          }
        }
      }
      y[0] = 0;
      final Consumer<SubSequence> _function_9 = (SubSequence subSequence) -> {
        int _j = subSequence.getJ();
        boolean _tripleNotEquals = (_j != 0);
        if (_tripleNotEquals) {
          int _i = subSequence.getI();
          int _plus_5 = (_i + 1);
          int _j_1 = subSequence.getJ();
          IntegerRange _upTo_1 = new IntegerRange(_plus_5, _j_1);
          for (final Integer i_1 : _upTo_1) {
            emptySS[((i_1).intValue() - 1)] = 1;
          }
        }
        x[0] = 0;
        int _i_1 = subSequence.getI();
        int _multiply = (_i_1 * yOffset);
        y[0] = _multiply;
        final Consumer<ActionC> _function_10 = (ActionC actionC) -> {
          int _i_2 = subSequence.getI();
          int _minus = (_i_2 - 1);
          emptySS[_minus] = 1;
          final Function1<Human, String> _function_11 = (Human it) -> {
            return it.getName();
          };
          boolean _contains_2 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_11).contains(sequence.getAgent().getName());
          if (_contains_2) {
            int _get_6 = x[0];
            int _plus_6 = (_get_6 + xOffset);
            x[0] = _plus_6;
            builder.append(this.genLoc(id[0], x[0], y[0], String.format("A%d_a%d_%s_busy", Integer.valueOf(AgentID[0]), Integer.valueOf(actionID[0]), actionC.getActionType().toString()), "t&lt;=5", null, false, false));
          } else {
            int _get_7 = x[0];
            int _plus_7 = (_get_7 + xOffset);
            x[0] = _plus_7;
            builder.append(this.genLoc(id[0], x[0], y[0], String.format("A%d_a%d_%s_busy", Integer.valueOf(AgentID[0]), Integer.valueOf(actionID[0]), actionC.getActionType().toString()), null, null, false, false));
          }
          int _get_8 = x[0];
          boolean _equals = (_get_8 == xOffset);
          if (_equals) {
            firstActionID[subSequence.getI()] = id[0];
          }
          int _get_9 = id[0];
          int _plus_8 = (_get_9 + 1);
          id[0] = _plus_8;
          int _get_10 = x[0];
          int _plus_9 = (_get_10 + xOffset);
          x[0] = _plus_9;
          builder.append(this.genLoc(id[0], x[0], y[0], String.format("A%d_a%d_%s_complete", Integer.valueOf(AgentID[0]), Integer.valueOf(actionID[0]), actionC.getActionType().toString()), null, null, false, true));
          lastActionID[subSequence.getI()] = id[0];
          int _get_11 = id[0];
          int _plus_10 = (_get_11 + 1);
          id[0] = _plus_10;
          int _get_12 = actionID[0];
          int _plus_11 = (_get_12 + 1);
          actionID[0] = _plus_11;
        };
        subSequence.getActionC().forEach(_function_10);
        actionCount[0] = 0;
        final Consumer<ActionC> _function_11 = (ActionC actionC) -> {
          int _get_6 = actionCount[0];
          int _plus_6 = (_get_6 + 1);
          actionCount[0] = _plus_6;
          boolean _isEmpty = actionC.getConditionalStatement().isEmpty();
          boolean _not = (!_isEmpty);
          if (_not) {
            final Consumer<ConditionalStatement> _function_12 = (ConditionalStatement cs) -> {
              final Consumer<Condition> _function_13 = (Condition c) -> {
                final AtomicPredicate pred0 = c.getAtomicPredicate().get(0);
                String _ap = pred0.getAp();
                boolean _equals = Objects.equals(_ap, "position");
                if (_equals) {
                  boolean _isNegated = pred0.isNegated();
                  if (_isNegated) {
                    String _apPositionGuard = this.apPositionGuard(model, pred0.getAgent().getName().toString(), pred0.getLocation().getName().toString());
                    String _plus_7 = ("not (" + _apPositionGuard);
                    String _plus_8 = (_plus_7 + ")");
                    guard.append(_plus_8);
                  } else {
                    String _apPositionGuard_1 = this.apPositionGuard(model, pred0.getAgent().getName().toString(), pred0.getLocation().getName().toString());
                    String _plus_9 = ("(" + _apPositionGuard_1);
                    String _plus_10 = (_plus_9 + ")");
                    guard.append(_plus_10);
                  }
                }
                int _size = c.getAtomicPredicate().size();
                boolean _equals_1 = (_size == 1);
                if (_equals_1) {
                } else {
                  int _size_1 = c.getAtomicPredicate().size();
                  int _minus = (_size_1 - 1);
                  ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _minus, true);
                  for (final Integer i_2 : _doubleDotLessThan) {
                    {
                      final AtomicPredicate pred2 = c.getAtomicPredicate().get(((i_2).intValue() + 1));
                      final String operatore = c.getOp().get((i_2).intValue());
                      String _string = operatore.toString();
                      boolean _equals_2 = Objects.equals(_string, "&&");
                      if (_equals_2) {
                        guard.append(" and(");
                      } else {
                        String _string_1 = operatore.toString();
                        boolean _equals_3 = Objects.equals(_string_1, "||");
                        if (_equals_3) {
                          guard.append(" or(");
                        }
                      }
                      boolean _isNegated_1 = pred2.isNegated();
                      if (_isNegated_1) {
                        String _apPositionGuard_2 = this.apPositionGuard(model, pred2.getAgent().getName().toString(), pred2.getLocation().getName().toString());
                        String _plus_11 = ("\nnot (" + _apPositionGuard_2);
                        String _plus_12 = (_plus_11 + "))");
                        guard.append(_plus_12);
                      } else {
                        String _apPositionGuard_3 = this.apPositionGuard(model, pred2.getAgent().getName().toString(), pred2.getLocation().getName().toString());
                        String _plus_13 = ("\n(" + _apPositionGuard_3);
                        String _plus_14 = (_plus_13 + "))");
                        guard.append(_plus_14);
                      }
                    }
                  }
                }
              };
              cs.getCondition().forEach(_function_13);
              final Function1<Human, String> _function_14 = (Human it) -> {
                return it.getName();
              };
              boolean _contains_2 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_14).contains(sequence.getAgent().getName());
              if (_contains_2) {
                String _string = actionC.getActionType().toString();
                String _plus_7 = ("_h_" + _string);
                String _plus_8 = (_plus_7 + "[H-1]!");
                sync.append(_plus_8);
              }
              final Function1<Robot, String> _function_15 = (Robot it) -> {
                return it.getName();
              };
              boolean _contains_3 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_15).contains(sequence.getAgent().getName());
              if (_contains_3) {
                String _string_1 = actionC.getActionType().toString();
                String _plus_9 = ("_r_" + _string_1);
                String _plus_10 = (_plus_9 + "[R-1]!");
                sync.append(_plus_10);
              }
              final Function1<Human, String> _function_16 = (Human it) -> {
                return it.getName();
              };
              boolean _contains_4 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_16).contains(sequence.getAgent().getName());
              if (_contains_4) {
                String _string_2 = actionC.getActionType().toString();
                boolean _equals = Objects.equals(_string_2, "moveTo");
                if (_equals) {
                  String targetName = actionC.getTarget().getName().toString();
                  int targetIndex = (-1);
                  int locIndex = ((List<String>)Conversions.doWrapArray(locations)).indexOf(targetName);
                  if ((locIndex != (-1))) {
                    targetIndex = locIndex;
                  } else {
                    int resIndex = ((List<String>)Conversions.doWrapArray(resources)).indexOf(targetName);
                    if ((resIndex != (-1))) {
                      int _length = locations.length;
                      int _plus_11 = (_length + resIndex);
                      targetIndex = _plus_11;
                    }
                  }
                  if ((targetIndex != (-1))) {
                    update.append(String.format("human_loc_target[H-1] = %d", Integer.valueOf(targetIndex)));
                  }
                }
              }
              final Function1<Robot, String> _function_17 = (Robot it) -> {
                return it.getName();
              };
              boolean _contains_5 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_17).contains(sequence.getAgent().getName());
              if (_contains_5) {
                String _string_3 = actionC.getActionType().toString();
                boolean _equals_1 = Objects.equals(_string_3, "moveTo");
                if (_equals_1) {
                  String targetName_1 = actionC.getTarget().getName().toString();
                  int targetIndex_1 = (-1);
                  int locIndex_1 = ((List<String>)Conversions.doWrapArray(locations)).indexOf(targetName_1);
                  if ((locIndex_1 != (-1))) {
                    targetIndex_1 = locIndex_1;
                  } else {
                    int resIndex_1 = ((List<String>)Conversions.doWrapArray(resources)).indexOf(targetName_1);
                    if ((resIndex_1 != (-1))) {
                      int _length_1 = locations.length;
                      int _plus_12 = (_length_1 + resIndex_1);
                      targetIndex_1 = _plus_12;
                    }
                  }
                  if ((targetIndex_1 != (-1))) {
                    update.append(String.format("robot_loc_target[R-1] = %d", Integer.valueOf(targetIndex_1)));
                  }
                }
                String _string_4 = actionC.getActionType().toString();
                boolean _equals_2 = Objects.equals(_string_4, "follow");
                if (_equals_2) {
                  update.append(String.format("f_loc_target[R-1] = %d,\n", Integer.valueOf(((List<String>)Conversions.doWrapArray(locations)).indexOf(actionC.getTarget().getName().toString()))));
                  final Function1<Human, String> _function_18 = (Human it) -> {
                    return it.getName();
                  };
                  boolean _contains_6 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_18).contains(actionC.getAgent().getName());
                  if (_contains_6) {
                    final Function1<Human, String> _function_19 = (Human it) -> {
                      return it.getName();
                    };
                    int _indexOf = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_19).indexOf(actionC.getAgent().getName());
                    int _plus_13 = (_indexOf + 1);
                    update.append(String.format("human_to_be_followed[R-1] = %d,\n", Integer.valueOf(_plus_13)));
                    update.append("robot_to_be_followed[R-1] = 0");
                  }
                  final Function1<Robot, String> _function_20 = (Robot it) -> {
                    return it.getName();
                  };
                  boolean _contains_7 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_20).contains(actionC.getAgent().getName());
                  if (_contains_7) {
                    final Function1<Robot, String> _function_21 = (Robot it) -> {
                      return it.getName();
                    };
                    int _indexOf_1 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_21).indexOf(actionC.getAgent().getName());
                    int _plus_14 = (_indexOf_1 + 1);
                    update.append(String.format("robot_to_be_followed[R-1] = %d,\n", Integer.valueOf(_plus_14)));
                    update.append("human_to_be_followed[R-1] = 0");
                  }
                }
                String _string_5 = actionC.getActionType().toString();
                boolean _equals_3 = Objects.equals(_string_5, "pick");
                if (_equals_3) {
                  update.append(String.format("robot_res_to_pick[R-1] = %d, old_inventory = robot_inventory", Integer.valueOf(((List<String>)Conversions.doWrapArray(resources)).indexOf(actionC.getResource().getName().toString()))));
                }
                String _string_6 = actionC.getActionType().toString();
                boolean _equals_4 = Objects.equals(_string_6, "deposit");
                if (_equals_4) {
                  update.append(String.format("robot_res_to_deposit[R-1] = %d, old_inventory = robot_inventory", Integer.valueOf(((List<String>)Conversions.doWrapArray(resources)).indexOf(actionC.getResource().getName().toString()))));
                }
              }
              String _cs1 = cs.getCs1();
              boolean _tripleNotEquals_1 = (_cs1 != null);
              if (_tripleNotEquals_1) {
                int _get_7 = firstActionID[subSequence.getI()];
                int _get_8 = actionCount[0];
                int _multiply_1 = (_get_8 * 2);
                int _plus_15 = (_get_7 + _multiply_1);
                int _minus = (_plus_15 - 2);
                int _get_9 = firstActionID[subSequence.getI()];
                int _get_10 = actionCount[0];
                int _multiply_2 = (_get_10 * 2);
                int _plus_16 = (_get_9 + _multiply_2);
                int _minus_1 = (_plus_16 - 1);
                String _string_7 = guard.toString();
                String _string_8 = sync.toString();
                String _plus_17 = ("stop" + _string_8);
                int _get_11 = actionCount[0];
                int _multiply_3 = (_get_11 * 2);
                int _minus_2 = (_multiply_3 - 2);
                int _multiply_4 = (xOffset * _minus_2);
                int _plus_18 = (_multiply_4 + xOffset);
                int _i_2 = subSequence.getI();
                int _multiply_5 = (_i_2 * yOffset);
                int _minus_3 = (_multiply_5 - 100);
                int _get_12 = actionCount[0];
                int _multiply_6 = (_get_12 * 2);
                int _minus_4 = (_multiply_6 - 2);
                int _multiply_7 = (xOffset * _minus_4);
                int _plus_19 = (_multiply_7 + xOffset);
                int _plus_20 = (_plus_19 + 100);
                int _i_3 = subSequence.getI();
                int _multiply_8 = (_i_3 * yOffset);
                int _minus_5 = (_multiply_8 - 100);
                transition.append(this.genTran(id[0], _minus, _minus_1, null, _string_7, _plus_17, null, true, _plus_18, _minus_3, _plus_20, _minus_5));
                int _get_13 = id[0];
                int _plus_21 = (_get_13 + 1);
                id[0] = _plus_21;
              }
              String _cs2 = cs.getCs2();
              boolean _tripleNotEquals_2 = (_cs2 != null);
              if (_tripleNotEquals_2) {
                String _string_9 = cs.getAction().getActionType().toString();
                boolean _equals_5 = Objects.equals(_string_9, "stop");
                if (_equals_5) {
                  int _get_14 = actionCount[0];
                  int _multiply_9 = (_get_14 * xOffset);
                  int _multiply_10 = (_multiply_9 * 2);
                  int _minus_6 = (_multiply_10 - xOffset);
                  int _i_4 = subSequence.getI();
                  int _multiply_11 = (_i_4 * yOffset);
                  int _minus_7 = (_multiply_11 - (yOffset / 2));
                  builder.append(this.genLoc(id[0], _minus_6, _minus_7, String.format("A%d_a%d_%s", Integer.valueOf(AgentID[0]), Integer.valueOf(actionID[0]), cs.getAction().getActionType().toString()), null, null, false, false));
                  int _get_15 = id[0];
                  int _plus_22 = (_get_15 + 1);
                  id[0] = _plus_22;
                  int _get_16 = actionID[0];
                  int _plus_23 = (_get_16 + 1);
                  actionID[0] = _plus_23;
                  int _get_17 = firstActionID[subSequence.getI()];
                  int _get_18 = actionCount[0];
                  int _multiply_12 = (_get_18 * 2);
                  int _plus_24 = (_get_17 + _multiply_12);
                  int _minus_8 = (_plus_24 - 2);
                  int _get_19 = id[0];
                  int _minus_9 = (_get_19 - 1);
                  String _string_10 = guard.toString();
                  String _plus_25 = ("not (" + _string_10);
                  String _plus_26 = (_plus_25 + ")");
                  String _string_11 = sync.toString();
                  String _plus_27 = ("stop" + _string_11);
                  int _get_20 = actionCount[0];
                  int _multiply_13 = (xOffset * _get_20);
                  int _multiply_14 = (_multiply_13 * 2);
                  int _plus_28 = (_multiply_14 + xOffset);
                  int _i_5 = subSequence.getI();
                  int _multiply_15 = (_i_5 * yOffset);
                  int _minus_10 = (_multiply_15 - 100);
                  transition.append(this.genTran(id[0], _minus_8, _minus_9, null, _plus_26, _plus_27, null, true, _plus_28, _minus_10, 0, 0));
                  int _get_21 = id[0];
                  int _plus_29 = (_get_21 + 1);
                  id[0] = _plus_29;
                  int _get_22 = actionCount[0];
                  boolean _equals_6 = (_get_22 == 1);
                  if (_equals_6) {
                    int _get_23 = id[0];
                    int _minus_11 = (_get_23 - 2);
                    int _i_6 = subSequence.getI();
                    String _string_12 = guard.toString();
                    String _string_13 = sync.toString();
                    String _plus_30 = ("stop" + _string_13);
                    String _string_14 = update.toString();
                    int _get_24 = actionCount[0];
                    int _multiply_16 = (xOffset * _get_24);
                    int _multiply_17 = (_multiply_16 * 2);
                    int _plus_31 = (_multiply_17 + xOffset);
                    int _i_7 = subSequence.getI();
                    int _multiply_18 = (_i_7 * yOffset);
                    int _minus_12 = (_multiply_18 - 100);
                    transition.append(this.genTran(id[0], _minus_11, _i_6, null, _string_12, _plus_30, _string_14, true, _plus_31, _minus_12, 0, 0));
                    int _get_25 = id[0];
                    int _plus_32 = (_get_25 + 1);
                    id[0] = _plus_32;
                  } else {
                    int _get_26 = id[0];
                    int _minus_13 = (_get_26 - 2);
                    int _get_27 = firstActionID[subSequence.getI()];
                    int _get_28 = actionCount[0];
                    int _multiply_19 = (_get_28 * 2);
                    int _plus_33 = (_get_27 + _multiply_19);
                    int _minus_14 = (_plus_33 - 3);
                    String _string_15 = guard.toString();
                    String _string_16 = sync.toString();
                    String _plus_34 = ("stop" + _string_16);
                    String _string_17 = update.toString();
                    int _get_29 = actionCount[0];
                    int _multiply_20 = (xOffset * _get_29);
                    int _multiply_21 = (_multiply_20 * 2);
                    int _plus_35 = (_multiply_21 + xOffset);
                    int _i_8 = subSequence.getI();
                    int _multiply_22 = (_i_8 * yOffset);
                    int _minus_15 = (_multiply_22 - 100);
                    transition.append(this.genTran(id[0], _minus_13, _minus_14, null, _string_15, _plus_34, _string_17, true, _plus_35, _minus_15, 0, 0));
                    int _get_30 = id[0];
                    int _plus_36 = (_get_30 + 1);
                    id[0] = _plus_36;
                  }
                }
                String _string_18 = cs.getAction().getActionType().toString();
                boolean _equals_7 = Objects.equals(_string_18, "moveTo");
                if (_equals_7) {
                }
              }
              String _cs3 = cs.getCs3();
              boolean _tripleNotEquals_3 = (_cs3 != null);
              if (_tripleNotEquals_3) {
                int _get_31 = x[0];
                int _minus_16 = (_get_31 - xOffset);
                int _get_32 = y[0];
                int _minus_17 = (_get_32 - (yOffset / 2));
                builder.append(this.genLoc(id[0], _minus_16, _minus_17, String.format("A%d_a%d_%s", Integer.valueOf(AgentID[0]), Integer.valueOf(actionID[0]), cs.getAction().getActionType().toString()), null, null, false, false));
                int _get_33 = id[0];
                int _plus_37 = (_get_33 + 1);
                id[0] = _plus_37;
                int _get_34 = actionID[0];
                int _plus_38 = (_get_34 + 1);
                actionID[0] = _plus_38;
              }
              String _cs4 = cs.getCs4();
              boolean _tripleNotEquals_4 = (_cs4 != null);
              if (_tripleNotEquals_4) {
              }
              guard.setLength(0);
              sync.setLength(0);
              update.setLength(0);
            };
            actionC.getConditionalStatement().forEach(_function_12);
          }
        };
        subSequence.getActionC().forEach(_function_11);
        int _i_2 = subSequence.getI();
        int _minus = (_i_2 - 1);
        int _get_6 = emptySS[_minus];
        boolean _equals = (_get_6 == 1);
        if (_equals) {
          int _get_7 = x[0];
          int _plus_6 = (_get_7 + xOffset);
          x[0] = _plus_6;
          builder.append(this.genLoc(id[0], x[0], y[0], String.format("s%d_end", Integer.valueOf(subSequence.getI())), null, null, false, false));
          int _get_8 = id[0];
          int _plus_7 = (_get_8 + 1);
          int _get_9 = lastActionID[subSequence.getI()];
          String _format = String.format("%s_stat = true", sequence.getAgent().getName().toLowerCase());
          int _get_10 = x[0];
          int _minus_1 = (_get_10 - xOffset);
          transition.append(this.genTran(_plus_7, _get_9, id[0], null, null, null, _format, true, _minus_1, y[0], 0, 0));
          endSubSeqID[subSequence.getI()] = id[0];
          int _get_11 = id[0];
          int _plus_8 = (_get_11 + 2);
          id[0] = _plus_8;
        }
      };
      sequence.getSubSequence().forEach(_function_9);
      int _get_6 = maxIndex[0];
      IntegerRange _upTo_1 = new IntegerRange(1, _get_6);
      for (final Integer i_1 : _upTo_1) {
        {
          int _get_7 = emptySS[((i_1).intValue() - 1)];
          boolean _equals = (_get_7 == 0);
          if (_equals) {
            x[0] = xOffset;
            y[0] = ((i_1).intValue() * yOffset);
            builder.append(this.genLoc(id[0], x[0], y[0], String.format("S%d_idle", i_1), null, null, false, false));
            endSubSeqID[(i_1).intValue()] = id[0];
            int _get_8 = id[0];
            int _plus_5 = (_get_8 + 1);
            id[0] = _plus_5;
          }
          emptySS[((i_1).intValue() - 1)] = 0;
        }
      }
      final Consumer<SubSequence> _function_10 = (SubSequence subSequence) -> {
        int _j = subSequence.getJ();
        int _i = subSequence.getI();
        int _minus = (_j - _i);
        boolean _greaterThan = (_minus > 0);
        if (_greaterThan) {
          int _j_1 = subSequence.getJ();
          boolean _tripleNotEquals = (_j_1 != 0);
          if (_tripleNotEquals) {
            int _i_1 = subSequence.getI();
            int _plus_5 = (_i_1 + 1);
            int _j_2 = subSequence.getJ();
            IntegerRange _upTo_2 = new IntegerRange(_plus_5, _j_2);
            for (final Integer i_2 : _upTo_2) {
              endSubSeqID[(i_2).intValue()] = endSubSeqID[((i_2).intValue() - 1)];
            }
          }
        }
      };
      sequence.getSubSequence().forEach(_function_10);
      builder.append(String.format("\t\t<init ref=\"id%d\"/>\n", Integer.valueOf(0)));
      final int[] firstAction = { 0 };
      actionCount[0] = 0;
      final HashMap<String, String> hMapGuard = new HashMap<String, String>();
      hMapGuard.put("moveTo", "humanPositionX[H-1] &lt;= location_X[human_loc_target[H-1]]+30 and\nhumanPositionX[H-1] &gt;= location_X[human_loc_target[H-1]]-30 and\nhumanPositionY[H-1] &lt;= location_Y[human_loc_target[H-1]]+30 and\nhumanPositionY[H-1] &gt;= location_Y[human_loc_target[H-1]]-30");
      final HashMap<String, String> rMapGuard = new HashMap<String, String>();
      rMapGuard.put("moveTo", "robPositionX[R-1] &lt;= location_X[robot_loc_target[R-1]]+30 and\nrobPositionX[R-1] &gt;= location_X[robot_loc_target[R-1]]-30 and\nrobPositionY[R-1] &lt;= location_Y[robot_loc_target[R-1]]+30 and\nrobPositionY[R-1] &gt;= location_Y[robot_loc_target[R-1]]-30");
      rMapGuard.put("followH", "humanPositionX[human_to_be_followed[R-1]-1] &lt;= location_X[f_loc_target[R-1]]+30 and\nhumanPositionX[human_to_be_followed[R-1]-1] &gt;= location_X[f_loc_target[R-1]]-30 and\nhumanPositionY[human_to_be_followed[R-1]-1] &lt;= location_Y[f_loc_target[R-1]]+30 and\nhumanPositionY[human_to_be_followed[R-1]-1] &gt;= location_Y[f_loc_target[R-1]]-30 and\nrobPositionX[R-1] &gt;= location_X[f_loc_target[R-1]]-30 and\nrobPositionX[R-1] &lt;= location_X[f_loc_target[R-1]]+30 and\nrobPositionY[R-1] &gt;= location_Y[f_loc_target[R-1]]-30 and\nrobPositionY[R-1] &lt;= location_Y[f_loc_target[R-1]]+30");
      rMapGuard.put("followR", "robPositionX[robot_to_be_followed[R-1]] &lt;= location_X[f_loc_target[R-1]]+30 and\nrobPositionX[robot_to_be_followed[R-1]-1] &gt;= location_X[f_loc_target[R-1]]-30 and\nrobPositionX[robot_to_be_followed[R-1]-1] &lt;= location_Y[f_loc_target[R-1]]+30 and\nrobPositionX[robot_to_be_followed[R-1]-1] &gt;= location_Y[f_loc_target[R-1]]-30 and\nrobPositionX[R-1] &gt;= location_X[f_loc_target[R-1]]-30 and\nrobPositionX[R-1] &lt;= location_X[f_loc_target[R-1]]+30 and\nrobPositionY[R-1] &gt;= location_Y[f_loc_target[R-1]]-30 and\nrobPositionY[R-1] &lt;= location_Y[f_loc_target[R-1]]+30");
      rMapGuard.put("pick", "robot_inventory[R-1][robot_res_to_pick[R-1]] > old_inventory[R-1][robot_res_to_pick[R-1]]");
      rMapGuard.put("deposit", "robot_inventory[R-1][robot_res_to_deposit[R-1]] &lt; old_inventory[R-1][robot_res_to_deposit[R-1]]");
      final Consumer<SubSequence> _function_11 = (SubSequence subSequence) -> {
        int _j = subSequence.getJ();
        boolean _tripleNotEquals = (_j != 0);
        if (_tripleNotEquals) {
          int _i = subSequence.getI();
          int _plus_5 = (_i + 1);
          int _j_1 = subSequence.getJ();
          IntegerRange _upTo_2 = new IntegerRange(_plus_5, _j_1);
          for (final Integer i_2 : _upTo_2) {
            emptySS[((i_2).intValue() - 1)] = 1;
          }
        }
        firstAction[0] = 1;
        final Consumer<ActionC> _function_12 = (ActionC actionC) -> {
          int _i_1 = subSequence.getI();
          int _minus = (_i_1 - 1);
          emptySS[_minus] = 1;
          int _get_7 = firstAction[0];
          boolean _equals = (_get_7 == 1);
          if (_equals) {
            firstAction[0] = 0;
            String _string = actionC.getActionType().toString();
            boolean _equals_1 = Objects.equals(_string, "moveTo");
            if (_equals_1) {
              final Function1<Human, String> _function_13 = (Human it) -> {
                return it.getName();
              };
              boolean _contains_2 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_13).contains(sequence.getAgent().getName());
              if (_contains_2) {
                int _i_2 = subSequence.getI();
                int _get_8 = firstActionID[subSequence.getI()];
                String _format = String.format("start_h_moveTo[H-1]!");
                int _i_3 = subSequence.getI();
                int _multiply = (_i_3 * yOffset);
                transition.append(this.genTran(id[0], _i_2, _get_8, null, null, _format, "t = 0", true, 0, _multiply, 0, 0));
                int _get_9 = id[0];
                int _plus_6 = (_get_9 + 1);
                id[0] = _plus_6;
                String targetName = actionC.getTarget().getName().toString();
                int targetIndex = (-1);
                int locIndex = ((List<String>)Conversions.doWrapArray(locations)).indexOf(targetName);
                if ((locIndex != (-1))) {
                  targetIndex = locIndex;
                } else {
                  int resIndex = ((List<String>)Conversions.doWrapArray(resources)).indexOf(targetName);
                  if ((resIndex != (-1))) {
                    int _length = locations.length;
                    int _plus_7 = (_length + resIndex);
                    targetIndex = _plus_7;
                  }
                }
                if ((targetIndex != (-1))) {
                  int _i_4 = subSequence.getI();
                  int _minus_1 = (_i_4 - 1);
                  int _get_10 = endSubSeqID[_minus_1];
                  int _i_5 = subSequence.getI();
                  String _format_1 = String.format("s%d?", Integer.valueOf(subSequence.getI()));
                  String _format_2 = String.format("human_loc_target[H-1] = %d", Integer.valueOf(targetIndex));
                  int _i_6 = subSequence.getI();
                  int _multiply_1 = (_i_6 * yOffset);
                  int _minus_2 = (_multiply_1 - 100);
                  transition.append(this.genTran(id[0], _get_10, _i_5, null, null, _format_1, _format_2, true, 0, _minus_2, 0, 0));
                }
                int _get_11 = id[0];
                int _plus_8 = (_get_11 + 1);
                id[0] = _plus_8;
                int _get_12 = firstActionID[subSequence.getI()];
                int _i_7 = subSequence.getI();
                int _get_13 = actionCount[0];
                int _multiply_2 = (xOffset * _get_13);
                int _multiply_3 = (_multiply_2 * 2);
                int _i_8 = subSequence.getI();
                int _multiply_4 = (_i_8 * yOffset);
                int _plus_9 = (_multiply_4 + 100);
                int _get_14 = actionCount[0];
                int _multiply_5 = (xOffset * _get_14);
                int _multiply_6 = (_multiply_5 * 2);
                int _plus_10 = (_multiply_6 + 100);
                int _i_9 = subSequence.getI();
                int _multiply_7 = (_i_9 * yOffset);
                int _plus_11 = (_multiply_7 + 80);
                transition.append(this.genTran(id[0], _get_12, _i_7, null, "t&gt;=5", null, "t = 0", true, _multiply_3, _plus_9, _plus_10, _plus_11));
                int _get_15 = id[0];
                int _plus_12 = (_get_15 + 1);
                id[0] = _plus_12;
                guard.append(hMapGuard.get("moveTo"));
                int _size = subSequence.getActionC().size();
                boolean _greaterEqualsThan = (_size >= 2);
                if (_greaterEqualsThan) {
                  EList<ActionC> _actionC = subSequence.getActionC();
                  int _indexOf = subSequence.getActionC().indexOf(actionC);
                  int _plus_13 = (_indexOf + 1);
                  ActionC nextAction = _actionC.get(_plus_13);
                  boolean _equals_2 = nextAction.getActionType().toString().equals("moveTo");
                  if (_equals_2) {
                    String nextTargetName = nextAction.getTarget().getName().toString();
                    int nextTargetIndex = (-1);
                    int nextLocIndex = ((List<String>)Conversions.doWrapArray(locations)).indexOf(nextTargetName);
                    if ((nextLocIndex != (-1))) {
                      nextTargetIndex = nextLocIndex;
                    } else {
                      int nextResIndex = ((List<String>)Conversions.doWrapArray(resources)).indexOf(nextTargetName);
                      if ((nextResIndex != (-1))) {
                        int _length_1 = locations.length;
                        int _plus_14 = (_length_1 + nextResIndex);
                        nextTargetIndex = _plus_14;
                      }
                    }
                    if ((nextTargetIndex != (-1))) {
                      update.append(String.format("human_loc_target[H-1] = %d", Integer.valueOf(nextTargetIndex)));
                    }
                  }
                }
                int _get_16 = firstActionID[subSequence.getI()];
                int _get_17 = firstActionID[subSequence.getI()];
                int _plus_15 = (_get_17 + 1);
                String _string_1 = guard.toString();
                String _format_3 = String.format("stop_h_moveTo[H-1]!");
                String _string_2 = update.toString();
                int _i_10 = subSequence.getI();
                int _multiply_8 = (_i_10 * yOffset);
                int _minus_3 = (_multiply_8 - 100);
                transition.append(this.genTran(id[0], _get_16, _plus_15, null, _string_1, _format_3, _string_2, true, xOffset, _minus_3, 0, 0));
                int _get_18 = id[0];
                int _plus_16 = (_get_18 + 1);
                id[0] = _plus_16;
                guard.setLength(0);
                update.setLength(0);
              }
              final Function1<Robot, String> _function_14 = (Robot it) -> {
                return it.getName();
              };
              boolean _contains_3 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_14).contains(sequence.getAgent().getName());
              if (_contains_3) {
                int _i_11 = subSequence.getI();
                int _get_19 = firstActionID[subSequence.getI()];
                String _format_4 = String.format("start_r_moveTo[R-1]!");
                int _i_12 = subSequence.getI();
                int _multiply_9 = (_i_12 * yOffset);
                transition.append(this.genTran(id[0], _i_11, _get_19, null, null, _format_4, null, true, 0, _multiply_9, 0, 0));
                int _get_20 = id[0];
                int _plus_17 = (_get_20 + 1);
                id[0] = _plus_17;
                String targetName_1 = actionC.getTarget().getName().toString();
                int targetIndex_1 = (-1);
                int locIndex_1 = ((List<String>)Conversions.doWrapArray(locations)).indexOf(targetName_1);
                if ((locIndex_1 != (-1))) {
                  targetIndex_1 = locIndex_1;
                } else {
                  int resIndex_1 = ((List<String>)Conversions.doWrapArray(resources)).indexOf(targetName_1);
                  if ((resIndex_1 != (-1))) {
                    int _length_2 = locations.length;
                    int _plus_18 = (_length_2 + resIndex_1);
                    targetIndex_1 = _plus_18;
                  }
                }
                if ((targetIndex_1 != (-1))) {
                  int _i_13 = subSequence.getI();
                  int _minus_4 = (_i_13 - 1);
                  int _get_21 = endSubSeqID[_minus_4];
                  int _i_14 = subSequence.getI();
                  String _format_5 = String.format("s%d?", Integer.valueOf(subSequence.getI()));
                  String _format_6 = String.format("robot_loc_target[R-1] = %d", Integer.valueOf(targetIndex_1));
                  int _i_15 = subSequence.getI();
                  int _multiply_10 = (_i_15 * yOffset);
                  int _minus_5 = (_multiply_10 - 100);
                  transition.append(this.genTran(id[0], _get_21, _i_14, null, null, _format_5, _format_6, true, 0, _minus_5, 0, 0));
                }
                int _get_22 = id[0];
                int _plus_19 = (_get_22 + 1);
                id[0] = _plus_19;
                guard.append(rMapGuard.get("moveTo"));
                int _indexOf_1 = subSequence.getActionC().indexOf(actionC);
                int _size_1 = subSequence.getActionC().size();
                int _minus_6 = (_size_1 - 1);
                boolean _lessThan = (_indexOf_1 < _minus_6);
                if (_lessThan) {
                  EList<ActionC> _actionC_1 = subSequence.getActionC();
                  int _indexOf_2 = subSequence.getActionC().indexOf(actionC);
                  int _plus_20 = (_indexOf_2 + 1);
                  ActionC nextAction_1 = _actionC_1.get(_plus_20);
                  boolean _equals_3 = nextAction_1.getActionType().toString().equals("moveTo");
                  if (_equals_3) {
                    String nextTargetName_1 = nextAction_1.getTarget().getName().toString();
                    int nextTargetIndex_1 = (-1);
                    int nextLocIndex_1 = ((List<String>)Conversions.doWrapArray(locations)).indexOf(nextTargetName_1);
                    if ((nextLocIndex_1 != (-1))) {
                      nextTargetIndex_1 = nextLocIndex_1;
                    } else {
                      int nextResIndex_1 = ((List<String>)Conversions.doWrapArray(resources)).indexOf(nextTargetName_1);
                      if ((nextResIndex_1 != (-1))) {
                        int _length_3 = locations.length;
                        int _plus_21 = (_length_3 + nextResIndex_1);
                        nextTargetIndex_1 = _plus_21;
                      }
                    }
                    if ((nextTargetIndex_1 != (-1))) {
                      update.append(String.format("robot_loc_target[R-1] = %d", Integer.valueOf(nextTargetIndex_1)));
                    }
                  }
                  boolean _equals_4 = nextAction_1.getActionType().toString().equals("follow");
                  if (_equals_4) {
                    update.append(String.format("f_loc_target[R-1] = %d,\n", Integer.valueOf(((List<String>)Conversions.doWrapArray(locations)).indexOf(nextAction_1.getTarget().getName().toString()))));
                    final Function1<Human, String> _function_15 = (Human it) -> {
                      return it.getName();
                    };
                    boolean _contains_4 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_15).contains(nextAction_1.getAgent().getName());
                    if (_contains_4) {
                      final Function1<Human, String> _function_16 = (Human it) -> {
                        return it.getName();
                      };
                      int _indexOf_3 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_16).indexOf(nextAction_1.getAgent().getName());
                      int _plus_22 = (_indexOf_3 + 1);
                      update.append(String.format("human_to_be_followed[R-1] = %d,\n", Integer.valueOf(_plus_22)));
                      update.append("robot_to_be_followed[R-1] = 0");
                    }
                    final Function1<Robot, String> _function_17 = (Robot it) -> {
                      return it.getName();
                    };
                    boolean _contains_5 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_17).contains(nextAction_1.getAgent().getName());
                    if (_contains_5) {
                      final Function1<Robot, String> _function_18 = (Robot it) -> {
                        return it.getName();
                      };
                      int _indexOf_4 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_18).indexOf(nextAction_1.getAgent().getName());
                      int _plus_23 = (_indexOf_4 + 1);
                      update.append(String.format("robot_to_be_followed[R-1] = %d,\n", Integer.valueOf(_plus_23)));
                      update.append("human_to_be_followed[R-1] = 0");
                    }
                  }
                  boolean _equals_5 = nextAction_1.getActionType().toString().equals("pick");
                  if (_equals_5) {
                    update.append(String.format("robot_res_to_pick[R-1] = %d, old_inventory = robot_inventory", Integer.valueOf(((List<String>)Conversions.doWrapArray(resources)).indexOf(nextAction_1.getResource().getName().toString()))));
                  }
                  boolean _equals_6 = nextAction_1.getActionType().toString().equals("deposit");
                  if (_equals_6) {
                    update.append(String.format("robot_res_to_deposit[R-1] = %d, old_inventory = robot_inventory", Integer.valueOf(((List<String>)Conversions.doWrapArray(resources)).indexOf(nextAction_1.getResource().getName().toString()))));
                  }
                }
                int _get_23 = firstActionID[subSequence.getI()];
                int _get_24 = firstActionID[subSequence.getI()];
                int _plus_24 = (_get_24 + 1);
                String _string_3 = guard.toString();
                String _format_7 = String.format("stop_r_moveTo[R-1]!");
                String _string_4 = update.toString();
                int _i_16 = subSequence.getI();
                int _multiply_11 = (_i_16 * yOffset);
                int _minus_7 = (_multiply_11 - 100);
                transition.append(this.genTran(id[0], _get_23, _plus_24, null, _string_3, _format_7, _string_4, true, xOffset, _minus_7, 0, 0));
                int _get_25 = id[0];
                int _plus_25 = (_get_25 + 1);
                id[0] = _plus_25;
                guard.setLength(0);
                update.setLength(0);
              }
            }
            String _string_5 = actionC.getActionType().toString();
            boolean _equals_7 = Objects.equals(_string_5, "follow");
            if (_equals_7) {
              final Function1<Robot, String> _function_19 = (Robot it) -> {
                return it.getName();
              };
              boolean _contains_6 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_19).contains(sequence.getAgent().getName());
              if (_contains_6) {
                int _i_17 = subSequence.getI();
                int _get_26 = firstActionID[subSequence.getI()];
                String _format_8 = String.format("start_r_follow[R-1]!");
                int _i_18 = subSequence.getI();
                int _multiply_12 = (_i_18 * yOffset);
                transition.append(this.genTran(id[0], _i_17, _get_26, null, null, _format_8, null, true, 0, _multiply_12, 0, 0));
                int _get_27 = id[0];
                int _plus_26 = (_get_27 + 1);
                id[0] = _plus_26;
                update.append(String.format("f_loc_target[R-1] = %d,\n", Integer.valueOf(((List<String>)Conversions.doWrapArray(locations)).indexOf(actionC.getTarget().getName().toString()))));
                final Function1<Human, String> _function_20 = (Human it) -> {
                  return it.getName();
                };
                boolean _contains_7 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_20).contains(actionC.getAgent().getName());
                if (_contains_7) {
                  final Function1<Human, String> _function_21 = (Human it) -> {
                    return it.getName();
                  };
                  int _indexOf_5 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_21).indexOf(actionC.getAgent().getName());
                  int _plus_27 = (_indexOf_5 + 1);
                  update.append(String.format("human_to_be_followed[R-1] = %d,\n", Integer.valueOf(_plus_27)));
                  update.append("robot_to_be_followed[R-1] = 0");
                }
                final Function1<Robot, String> _function_22 = (Robot it) -> {
                  return it.getName();
                };
                boolean _contains_8 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_22).contains(actionC.getAgent().getName());
                if (_contains_8) {
                  final Function1<Robot, String> _function_23 = (Robot it) -> {
                    return it.getName();
                  };
                  int _indexOf_6 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_23).indexOf(actionC.getAgent().getName());
                  int _plus_28 = (_indexOf_6 + 1);
                  update.append(String.format("robot_to_be_followed[R-1] = %d,\n", Integer.valueOf(_plus_28)));
                  update.append("human_to_be_followed[R-1] = 0");
                }
                int _i_19 = subSequence.getI();
                int _minus_8 = (_i_19 - 1);
                int _get_28 = endSubSeqID[_minus_8];
                int _i_20 = subSequence.getI();
                String _format_9 = String.format("s%d?", Integer.valueOf(subSequence.getI()));
                String _string_6 = update.toString();
                int _i_21 = subSequence.getI();
                int _multiply_13 = (_i_21 * yOffset);
                int _minus_9 = (_multiply_13 - 100);
                transition.append(this.genTran(id[0], _get_28, _i_20, null, null, _format_9, _string_6, true, 0, _minus_9, 0, 0));
                int _get_29 = id[0];
                int _plus_29 = (_get_29 + 1);
                id[0] = _plus_29;
                update.setLength(0);
                final Function1<Human, String> _function_24 = (Human it) -> {
                  return it.getName();
                };
                boolean _contains_9 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_24).contains(actionC.getAgent().getName());
                if (_contains_9) {
                  guard.append(rMapGuard.get("followH"));
                }
                final Function1<Robot, String> _function_25 = (Robot it) -> {
                  return it.getName();
                };
                boolean _contains_10 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_25).contains(actionC.getAgent().getName());
                if (_contains_10) {
                  guard.append(rMapGuard.get("followR"));
                }
                int _indexOf_7 = subSequence.getActionC().indexOf(actionC);
                int _size_2 = subSequence.getActionC().size();
                int _minus_10 = (_size_2 - 1);
                boolean _lessThan_1 = (_indexOf_7 < _minus_10);
                if (_lessThan_1) {
                  EList<ActionC> _actionC_2 = subSequence.getActionC();
                  int _indexOf_8 = subSequence.getActionC().indexOf(actionC);
                  int _plus_30 = (_indexOf_8 + 1);
                  ActionC nextAction_2 = _actionC_2.get(_plus_30);
                  boolean _equals_8 = nextAction_2.getActionType().toString().equals("moveTo");
                  if (_equals_8) {
                    String nextTargetName_2 = nextAction_2.getTarget().getName().toString();
                    int nextTargetIndex_2 = (-1);
                    int nextLocIndex_2 = ((List<String>)Conversions.doWrapArray(locations)).indexOf(nextTargetName_2);
                    if ((nextLocIndex_2 != (-1))) {
                      nextTargetIndex_2 = nextLocIndex_2;
                    } else {
                      int nextResIndex_2 = ((List<String>)Conversions.doWrapArray(resources)).indexOf(nextTargetName_2);
                      if ((nextResIndex_2 != (-1))) {
                        int _length_4 = locations.length;
                        int _plus_31 = (_length_4 + nextResIndex_2);
                        nextTargetIndex_2 = _plus_31;
                      }
                    }
                    if ((nextTargetIndex_2 != (-1))) {
                      update.append(String.format("robot_loc_target[R-1] = %d", Integer.valueOf(nextTargetIndex_2)));
                    }
                  }
                  boolean _equals_9 = nextAction_2.getActionType().toString().equals("follow");
                  if (_equals_9) {
                    update.append(String.format("f_loc_target[R-1] = %d,\n", Integer.valueOf(((List<String>)Conversions.doWrapArray(locations)).indexOf(nextAction_2.getTarget().getName().toString()))));
                    final Function1<Human, String> _function_26 = (Human it) -> {
                      return it.getName();
                    };
                    boolean _contains_11 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_26).contains(nextAction_2.getAgent().getName());
                    if (_contains_11) {
                      final Function1<Human, String> _function_27 = (Human it) -> {
                        return it.getName();
                      };
                      int _indexOf_9 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_27).indexOf(nextAction_2.getAgent().getName());
                      int _plus_32 = (_indexOf_9 + 1);
                      update.append(String.format("human_to_be_followed[R-1] = %d,\n", Integer.valueOf(_plus_32)));
                      update.append("robot_to_be_followed[R-1] = 0");
                    }
                    final Function1<Robot, String> _function_28 = (Robot it) -> {
                      return it.getName();
                    };
                    boolean _contains_12 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_28).contains(nextAction_2.getAgent().getName());
                    if (_contains_12) {
                      final Function1<Robot, String> _function_29 = (Robot it) -> {
                        return it.getName();
                      };
                      int _indexOf_10 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_29).indexOf(nextAction_2.getAgent().getName());
                      int _plus_33 = (_indexOf_10 + 1);
                      update.append(String.format("robot_to_be_followed[R-1] = %d,\n", Integer.valueOf(_plus_33)));
                      update.append("human_to_be_followed[R-1] = 0");
                    }
                  }
                  boolean _equals_10 = nextAction_2.getActionType().toString().equals("pick");
                  if (_equals_10) {
                    update.append(String.format("robot_res_to_pick[R-1] = %d, old_inventory = robot_inventory", Integer.valueOf(((List<String>)Conversions.doWrapArray(resources)).indexOf(nextAction_2.getResource().getName().toString()))));
                  }
                  boolean _equals_11 = nextAction_2.getActionType().toString().equals("deposit");
                  if (_equals_11) {
                    update.append(String.format("robot_res_to_pdeposit[R-1] = %d, old_inventory = robot_inventory", Integer.valueOf(((List<String>)Conversions.doWrapArray(resources)).indexOf(nextAction_2.getResource().getName().toString()))));
                  }
                }
                int _get_30 = firstActionID[subSequence.getI()];
                int _get_31 = firstActionID[subSequence.getI()];
                int _plus_34 = (_get_31 + 1);
                String _string_7 = guard.toString();
                String _format_10 = String.format("stop_r_follow[R-1]!");
                String _string_8 = update.toString();
                int _i_22 = subSequence.getI();
                int _multiply_14 = (_i_22 * yOffset);
                int _minus_11 = (_multiply_14 - 100);
                transition.append(this.genTran(id[0], _get_30, _plus_34, null, _string_7, _format_10, _string_8, true, xOffset, _minus_11, 0, 0));
                int _get_32 = id[0];
                int _plus_35 = (_get_32 + 1);
                id[0] = _plus_35;
                guard.setLength(0);
                update.setLength(0);
              }
            }
            String _string_9 = actionC.getActionType().toString();
            boolean _equals_12 = Objects.equals(_string_9, "pick");
            if (_equals_12) {
              final Function1<Robot, String> _function_30 = (Robot it) -> {
                return it.getName();
              };
              boolean _contains_13 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_30).contains(sequence.getAgent().getName());
              if (_contains_13) {
                int _i_23 = subSequence.getI();
                int _get_33 = firstActionID[subSequence.getI()];
                String _format_11 = String.format("start_r_pick[R-1]!");
                int _i_24 = subSequence.getI();
                int _multiply_15 = (_i_24 * yOffset);
                transition.append(this.genTran(id[0], _i_23, _get_33, null, null, _format_11, null, true, 0, _multiply_15, 0, 0));
                int _get_34 = id[0];
                int _plus_36 = (_get_34 + 1);
                id[0] = _plus_36;
                update.append(String.format("robot_res_to_pick[R-1] = %d, old_inventory = robot_inventory", Integer.valueOf(((List<String>)Conversions.doWrapArray(resources)).indexOf(actionC.getResource().getName().toString()))));
                int _i_25 = subSequence.getI();
                int _minus_12 = (_i_25 - 1);
                int _get_35 = endSubSeqID[_minus_12];
                int _i_26 = subSequence.getI();
                String _format_12 = String.format("s%d?", Integer.valueOf(subSequence.getI()));
                String _string_10 = update.toString();
                int _i_27 = subSequence.getI();
                int _multiply_16 = (_i_27 * yOffset);
                int _minus_13 = (_multiply_16 - 100);
                transition.append(this.genTran(id[0], _get_35, _i_26, null, null, _format_12, _string_10, true, 0, _minus_13, 0, 0));
                int _get_36 = id[0];
                int _plus_37 = (_get_36 + 1);
                id[0] = _plus_37;
                update.setLength(0);
                guard.append(rMapGuard.get("pick"));
                int _indexOf_11 = subSequence.getActionC().indexOf(actionC);
                int _size_3 = subSequence.getActionC().size();
                int _minus_14 = (_size_3 - 1);
                boolean _lessThan_2 = (_indexOf_11 < _minus_14);
                if (_lessThan_2) {
                  EList<ActionC> _actionC_3 = subSequence.getActionC();
                  int _indexOf_12 = subSequence.getActionC().indexOf(actionC);
                  int _plus_38 = (_indexOf_12 + 1);
                  ActionC nextAction_3 = _actionC_3.get(_plus_38);
                  boolean _equals_13 = nextAction_3.getActionType().toString().equals("moveTo");
                  if (_equals_13) {
                    String nextTargetName_3 = nextAction_3.getTarget().getName().toString();
                    int nextTargetIndex_3 = (-1);
                    int nextLocIndex_3 = ((List<String>)Conversions.doWrapArray(locations)).indexOf(nextTargetName_3);
                    if ((nextLocIndex_3 != (-1))) {
                      nextTargetIndex_3 = nextLocIndex_3;
                    } else {
                      int nextResIndex_3 = ((List<String>)Conversions.doWrapArray(resources)).indexOf(nextTargetName_3);
                      if ((nextResIndex_3 != (-1))) {
                        int _length_5 = locations.length;
                        int _plus_39 = (_length_5 + nextResIndex_3);
                        nextTargetIndex_3 = _plus_39;
                      }
                    }
                    if ((nextTargetIndex_3 != (-1))) {
                      update.append(String.format("robot_loc_target[R-1] = %d", Integer.valueOf(nextTargetIndex_3)));
                    }
                  }
                  boolean _equals_14 = nextAction_3.getActionType().toString().equals("follow");
                  if (_equals_14) {
                    update.append(String.format("f_loc_target[R-1] = %d,\n", Integer.valueOf(((List<String>)Conversions.doWrapArray(locations)).indexOf(nextAction_3.getTarget().getName().toString()))));
                    final Function1<Human, String> _function_31 = (Human it) -> {
                      return it.getName();
                    };
                    boolean _contains_14 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_31).contains(nextAction_3.getAgent().getName());
                    if (_contains_14) {
                      final Function1<Human, String> _function_32 = (Human it) -> {
                        return it.getName();
                      };
                      int _indexOf_13 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_32).indexOf(nextAction_3.getAgent().getName());
                      int _plus_40 = (_indexOf_13 + 1);
                      update.append(String.format("human_to_be_followed[R-1] = %d,\n", Integer.valueOf(_plus_40)));
                      update.append("robot_to_be_followed[R-1] = 0");
                    }
                    final Function1<Robot, String> _function_33 = (Robot it) -> {
                      return it.getName();
                    };
                    boolean _contains_15 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_33).contains(nextAction_3.getAgent().getName());
                    if (_contains_15) {
                      final Function1<Robot, String> _function_34 = (Robot it) -> {
                        return it.getName();
                      };
                      int _indexOf_14 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_34).indexOf(nextAction_3.getAgent().getName());
                      int _plus_41 = (_indexOf_14 + 1);
                      update.append(String.format("robot_to_be_followed[R-1] = %d,\n", Integer.valueOf(_plus_41)));
                      update.append("human_to_be_followed[R-1] = 0");
                    }
                  }
                  boolean _equals_15 = nextAction_3.getActionType().toString().equals("pick");
                  if (_equals_15) {
                    update.append(String.format("robot_res_to_pick[R-1] = %d, old_inventory = robot_inventory", Integer.valueOf(((List<String>)Conversions.doWrapArray(resources)).indexOf(nextAction_3.getResource().getName().toString()))));
                  }
                  boolean _equals_16 = nextAction_3.getActionType().toString().equals("deposit");
                  if (_equals_16) {
                    update.append(String.format("robot_res_to_deposit[R-1] = %d, old_inventory = robot_inventory", Integer.valueOf(((List<String>)Conversions.doWrapArray(resources)).indexOf(nextAction_3.getResource().getName().toString()))));
                  }
                }
                int _get_37 = firstActionID[subSequence.getI()];
                int _get_38 = firstActionID[subSequence.getI()];
                int _plus_42 = (_get_38 + 1);
                String _string_11 = guard.toString();
                String _format_13 = String.format("stop_r_pick[R-1]!");
                String _string_12 = update.toString();
                int _i_28 = subSequence.getI();
                int _multiply_17 = (_i_28 * yOffset);
                int _minus_15 = (_multiply_17 - 100);
                transition.append(this.genTran(id[0], _get_37, _plus_42, null, _string_11, _format_13, _string_12, true, xOffset, _minus_15, 0, 0));
                int _get_39 = id[0];
                int _plus_43 = (_get_39 + 1);
                id[0] = _plus_43;
                guard.setLength(0);
                update.setLength(0);
              }
            }
            String _string_13 = actionC.getActionType().toString();
            boolean _equals_17 = Objects.equals(_string_13, "deposit");
            if (_equals_17) {
              final Function1<Robot, String> _function_35 = (Robot it) -> {
                return it.getName();
              };
              boolean _contains_16 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_35).contains(sequence.getAgent().getName());
              if (_contains_16) {
                int _i_29 = subSequence.getI();
                int _get_40 = firstActionID[subSequence.getI()];
                String _format_14 = String.format("start_r_deposit[R-1]!");
                int _i_30 = subSequence.getI();
                int _multiply_18 = (_i_30 * yOffset);
                transition.append(this.genTran(id[0], _i_29, _get_40, null, null, _format_14, null, true, 0, _multiply_18, 0, 0));
                int _get_41 = id[0];
                int _plus_44 = (_get_41 + 1);
                id[0] = _plus_44;
                update.append(String.format("robot_res_to_deposit[R-1] = %d, old_inventory = robot_inventory", Integer.valueOf(((List<String>)Conversions.doWrapArray(resources)).indexOf(actionC.getResource().getName().toString()))));
                int _i_31 = subSequence.getI();
                int _minus_16 = (_i_31 - 1);
                int _get_42 = endSubSeqID[_minus_16];
                int _i_32 = subSequence.getI();
                String _format_15 = String.format("s%d?", Integer.valueOf(subSequence.getI()));
                String _string_14 = update.toString();
                int _i_33 = subSequence.getI();
                int _multiply_19 = (_i_33 * yOffset);
                int _minus_17 = (_multiply_19 - 100);
                transition.append(this.genTran(id[0], _get_42, _i_32, null, null, _format_15, _string_14, true, 0, _minus_17, 0, 0));
                int _get_43 = id[0];
                int _plus_45 = (_get_43 + 1);
                id[0] = _plus_45;
                update.setLength(0);
                guard.append(rMapGuard.get("deposit"));
                int _indexOf_15 = subSequence.getActionC().indexOf(actionC);
                int _size_4 = subSequence.getActionC().size();
                int _minus_18 = (_size_4 - 1);
                boolean _lessThan_3 = (_indexOf_15 < _minus_18);
                if (_lessThan_3) {
                  EList<ActionC> _actionC_4 = subSequence.getActionC();
                  int _indexOf_16 = subSequence.getActionC().indexOf(actionC);
                  int _plus_46 = (_indexOf_16 + 1);
                  ActionC nextAction_4 = _actionC_4.get(_plus_46);
                  boolean _equals_18 = nextAction_4.getActionType().toString().equals("moveTo");
                  if (_equals_18) {
                    String nextTargetName_4 = nextAction_4.getTarget().getName().toString();
                    int nextTargetIndex_4 = (-1);
                    int nextLocIndex_4 = ((List<String>)Conversions.doWrapArray(locations)).indexOf(nextTargetName_4);
                    if ((nextLocIndex_4 != (-1))) {
                      nextTargetIndex_4 = nextLocIndex_4;
                    } else {
                      int nextResIndex_4 = ((List<String>)Conversions.doWrapArray(resources)).indexOf(nextTargetName_4);
                      if ((nextResIndex_4 != (-1))) {
                        int _length_6 = locations.length;
                        int _plus_47 = (_length_6 + nextResIndex_4);
                        nextTargetIndex_4 = _plus_47;
                      }
                    }
                    if ((nextTargetIndex_4 != (-1))) {
                      update.append(String.format("robot_loc_target[R-1] = %d", Integer.valueOf(nextTargetIndex_4)));
                    }
                  }
                  boolean _equals_19 = nextAction_4.getActionType().toString().equals("follow");
                  if (_equals_19) {
                    update.append(String.format("f_loc_target[R-1] = %d,\n", Integer.valueOf(((List<String>)Conversions.doWrapArray(locations)).indexOf(nextAction_4.getTarget().getName().toString()))));
                    final Function1<Human, String> _function_36 = (Human it) -> {
                      return it.getName();
                    };
                    boolean _contains_17 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_36).contains(nextAction_4.getAgent().getName());
                    if (_contains_17) {
                      final Function1<Human, String> _function_37 = (Human it) -> {
                        return it.getName();
                      };
                      int _indexOf_17 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_37).indexOf(nextAction_4.getAgent().getName());
                      int _plus_48 = (_indexOf_17 + 1);
                      update.append(String.format("human_to_be_followed[R-1] = %d,\n", Integer.valueOf(_plus_48)));
                      update.append("robot_to_be_followed[R-1] = 0");
                    }
                    final Function1<Robot, String> _function_38 = (Robot it) -> {
                      return it.getName();
                    };
                    boolean _contains_18 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_38).contains(nextAction_4.getAgent().getName());
                    if (_contains_18) {
                      final Function1<Robot, String> _function_39 = (Robot it) -> {
                        return it.getName();
                      };
                      int _indexOf_18 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_39).indexOf(nextAction_4.getAgent().getName());
                      int _plus_49 = (_indexOf_18 + 1);
                      update.append(String.format("robot_to_be_followed[R-1] = %d,\n", Integer.valueOf(_plus_49)));
                      update.append("human_to_be_followed[R-1] = 0");
                    }
                  }
                  boolean _equals_20 = nextAction_4.getActionType().toString().equals("pick");
                  if (_equals_20) {
                    update.append(String.format("robot_res_to_pick[R-1] = %d, old_inventory = robot_inventory", Integer.valueOf(((List<String>)Conversions.doWrapArray(resources)).indexOf(nextAction_4.getResource().getName().toString()))));
                  }
                  boolean _equals_21 = nextAction_4.getActionType().toString().equals("deposit");
                  if (_equals_21) {
                    update.append(String.format("robot_res_to_deposit[R-1] = %d, old_inventory = robot_inventory", Integer.valueOf(((List<String>)Conversions.doWrapArray(resources)).indexOf(nextAction_4.getResource().getName().toString()))));
                  }
                }
                int _get_44 = firstActionID[subSequence.getI()];
                int _get_45 = firstActionID[subSequence.getI()];
                int _plus_50 = (_get_45 + 1);
                String _string_15 = guard.toString();
                String _format_16 = String.format("stop_r_deposit[R-1]!");
                String _string_16 = update.toString();
                int _i_34 = subSequence.getI();
                int _multiply_20 = (_i_34 * yOffset);
                int _minus_19 = (_multiply_20 - 100);
                transition.append(this.genTran(id[0], _get_44, _plus_50, null, _string_15, _format_16, _string_16, true, xOffset, _minus_19, 0, 0));
                int _get_46 = id[0];
                int _plus_51 = (_get_46 + 1);
                id[0] = _plus_51;
                guard.setLength(0);
                update.setLength(0);
              }
            }
          } else {
            int _get_47 = actionCount[0];
            int _plus_52 = (_get_47 + 1);
            actionCount[0] = _plus_52;
            String _string_17 = actionC.getActionType().toString();
            boolean _equals_22 = Objects.equals(_string_17, "moveTo");
            if (_equals_22) {
              final Function1<Human, String> _function_40 = (Human it) -> {
                return it.getName();
              };
              boolean _contains_19 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_40).contains(sequence.getAgent().getName());
              if (_contains_19) {
                int _get_48 = firstActionID[subSequence.getI()];
                int _get_49 = actionCount[0];
                int _multiply_21 = (_get_49 * 2);
                int _plus_53 = (_get_48 + _multiply_21);
                int _minus_20 = (_plus_53 - 1);
                int _get_50 = firstActionID[subSequence.getI()];
                int _get_51 = actionCount[0];
                int _multiply_22 = (_get_51 * 2);
                int _plus_54 = (_get_50 + _multiply_22);
                String _format_17 = String.format("start_h_moveTo[H-1]!");
                int _get_52 = actionCount[0];
                int _multiply_23 = (xOffset * _get_52);
                int _multiply_24 = (_multiply_23 * 2);
                int _i_35 = subSequence.getI();
                int _multiply_25 = (_i_35 * yOffset);
                transition.append(this.genTran(id[0], _minus_20, _plus_54, null, null, _format_17, "t = 0", true, _multiply_24, _multiply_25, 0, 0));
                int _get_53 = id[0];
                int _plus_55 = (_get_53 + 1);
                id[0] = _plus_55;
                int _get_54 = firstActionID[subSequence.getI()];
                int _get_55 = actionCount[0];
                int _multiply_26 = (_get_55 * 2);
                int _plus_56 = (_get_54 + _multiply_26);
                int _get_56 = firstActionID[subSequence.getI()];
                int _get_57 = actionCount[0];
                int _multiply_27 = (_get_57 * 2);
                int _plus_57 = (_get_56 + _multiply_27);
                int _minus_21 = (_plus_57 - 1);
                int _get_58 = actionCount[0];
                int _multiply_28 = (xOffset * _get_58);
                int _multiply_29 = (_multiply_28 * 2);
                int _i_36 = subSequence.getI();
                int _multiply_30 = (_i_36 * yOffset);
                int _plus_58 = (_multiply_30 + 100);
                int _get_59 = actionCount[0];
                int _multiply_31 = (xOffset * _get_59);
                int _multiply_32 = (_multiply_31 * 2);
                int _plus_59 = (_multiply_32 + 100);
                int _i_37 = subSequence.getI();
                int _multiply_33 = (_i_37 * yOffset);
                int _plus_60 = (_multiply_33 + 80);
                transition.append(this.genTran(id[0], _plus_56, _minus_21, null, "t&gt;=5", null, "t = 0", true, _multiply_29, _plus_58, _plus_59, _plus_60));
                int _get_60 = id[0];
                int _plus_61 = (_get_60 + 1);
                id[0] = _plus_61;
                guard.append(hMapGuard.get("moveTo"));
                int _size_5 = subSequence.getActionC().size();
                int _get_61 = actionCount[0];
                int _plus_62 = (_get_61 + 2);
                boolean _greaterEqualsThan_1 = (_size_5 >= _plus_62);
                if (_greaterEqualsThan_1) {
                  EList<ActionC> _actionC_5 = subSequence.getActionC();
                  int _indexOf_19 = subSequence.getActionC().indexOf(actionC);
                  int _plus_63 = (_indexOf_19 + 1);
                  ActionC nextAction_5 = _actionC_5.get(_plus_63);
                  boolean _equals_23 = nextAction_5.getActionType().toString().equals("moveTo");
                  if (_equals_23) {
                    String nextTargetName_5 = nextAction_5.getTarget().getName().toString();
                    int nextTargetIndex_5 = (-1);
                    int nextLocIndex_5 = ((List<String>)Conversions.doWrapArray(locations)).indexOf(nextTargetName_5);
                    if ((nextLocIndex_5 != (-1))) {
                      nextTargetIndex_5 = nextLocIndex_5;
                    } else {
                      int nextResIndex_5 = ((List<String>)Conversions.doWrapArray(resources)).indexOf(nextTargetName_5);
                      if ((nextResIndex_5 != (-1))) {
                        int _length_7 = locations.length;
                        int _plus_64 = (_length_7 + nextResIndex_5);
                        nextTargetIndex_5 = _plus_64;
                      }
                    }
                    if ((nextTargetIndex_5 != (-1))) {
                      update.append(String.format("human_loc_target[R-1] = %d", Integer.valueOf(nextTargetIndex_5)));
                    }
                  }
                }
                int _get_62 = firstActionID[subSequence.getI()];
                int _get_63 = actionCount[0];
                int _multiply_34 = (_get_63 * 2);
                int _plus_65 = (_get_62 + _multiply_34);
                int _get_64 = firstActionID[subSequence.getI()];
                int _get_65 = actionCount[0];
                int _multiply_35 = (_get_65 * 2);
                int _plus_66 = (_get_64 + _multiply_35);
                int _plus_67 = (_plus_66 + 1);
                String _string_18 = guard.toString();
                String _format_18 = String.format("stop_h_moveTo[H-1]!");
                String _string_19 = update.toString();
                int _get_66 = actionCount[0];
                int _multiply_36 = (_get_66 * 2);
                int _plus_68 = (_multiply_36 + 1);
                int _multiply_37 = (xOffset * _plus_68);
                int _i_38 = subSequence.getI();
                int _multiply_38 = (_i_38 * yOffset);
                int _minus_22 = (_multiply_38 - 100);
                transition.append(this.genTran(id[0], _plus_65, _plus_67, null, _string_18, _format_18, _string_19, true, _multiply_37, _minus_22, 0, 0));
                int _get_67 = id[0];
                int _plus_69 = (_get_67 + 1);
                id[0] = _plus_69;
                guard.setLength(0);
                update.setLength(0);
              }
              final Function1<Robot, String> _function_41 = (Robot it) -> {
                return it.getName();
              };
              boolean _contains_20 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_41).contains(sequence.getAgent().getName());
              if (_contains_20) {
                int _get_68 = firstActionID[subSequence.getI()];
                int _get_69 = actionCount[0];
                int _multiply_39 = (_get_69 * 2);
                int _plus_70 = (_get_68 + _multiply_39);
                int _minus_23 = (_plus_70 - 1);
                int _get_70 = firstActionID[subSequence.getI()];
                int _get_71 = actionCount[0];
                int _multiply_40 = (_get_71 * 2);
                int _plus_71 = (_get_70 + _multiply_40);
                String _format_19 = String.format("start_r_moveTo[R-1]!");
                int _get_72 = actionCount[0];
                int _multiply_41 = (xOffset * _get_72);
                int _multiply_42 = (_multiply_41 * 2);
                int _i_39 = subSequence.getI();
                int _multiply_43 = (_i_39 * yOffset);
                transition.append(this.genTran(id[0], _minus_23, _plus_71, null, null, _format_19, null, true, _multiply_42, _multiply_43, 0, 0));
                int _get_73 = id[0];
                int _plus_72 = (_get_73 + 1);
                id[0] = _plus_72;
                guard.append(rMapGuard.get("moveTo"));
                int _size_6 = subSequence.getActionC().size();
                int _get_74 = actionCount[0];
                int _plus_73 = (_get_74 + 2);
                boolean _greaterEqualsThan_2 = (_size_6 >= _plus_73);
                if (_greaterEqualsThan_2) {
                  EList<ActionC> _actionC_6 = subSequence.getActionC();
                  int _indexOf_20 = subSequence.getActionC().indexOf(actionC);
                  int _plus_74 = (_indexOf_20 + 1);
                  ActionC nextAction_6 = _actionC_6.get(_plus_74);
                  boolean _equals_24 = nextAction_6.getActionType().toString().equals("moveTo");
                  if (_equals_24) {
                    String nextTargetName_6 = nextAction_6.getTarget().getName().toString();
                    int nextTargetIndex_6 = (-1);
                    int nextLocIndex_6 = ((List<String>)Conversions.doWrapArray(locations)).indexOf(nextTargetName_6);
                    if ((nextLocIndex_6 != (-1))) {
                      nextTargetIndex_6 = nextLocIndex_6;
                    } else {
                      int nextResIndex_6 = ((List<String>)Conversions.doWrapArray(resources)).indexOf(nextTargetName_6);
                      if ((nextResIndex_6 != (-1))) {
                        int _length_8 = locations.length;
                        int _plus_75 = (_length_8 + nextResIndex_6);
                        nextTargetIndex_6 = _plus_75;
                      }
                    }
                    if ((nextTargetIndex_6 != (-1))) {
                      update.append(String.format("robot_loc_target[R-1] = %d", Integer.valueOf(nextTargetIndex_6)));
                    }
                  }
                  boolean _equals_25 = nextAction_6.getActionType().toString().equals("follow");
                  if (_equals_25) {
                    update.append(String.format("f_loc_target[R-1] = %d,\n", Integer.valueOf(((List<String>)Conversions.doWrapArray(locations)).indexOf(nextAction_6.getTarget().getName().toString()))));
                    final Function1<Human, String> _function_42 = (Human it) -> {
                      return it.getName();
                    };
                    boolean _contains_21 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_42).contains(nextAction_6.getAgent().getName());
                    if (_contains_21) {
                      final Function1<Human, String> _function_43 = (Human it) -> {
                        return it.getName();
                      };
                      int _indexOf_21 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_43).indexOf(nextAction_6.getAgent().getName());
                      int _plus_76 = (_indexOf_21 + 1);
                      update.append(String.format("human_to_be_followed[R-1] = %d,\n", Integer.valueOf(_plus_76)));
                      update.append("robot_to_be_followed[R-1] = 0");
                    }
                    final Function1<Robot, String> _function_44 = (Robot it) -> {
                      return it.getName();
                    };
                    boolean _contains_22 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_44).contains(nextAction_6.getAgent().getName());
                    if (_contains_22) {
                      final Function1<Robot, String> _function_45 = (Robot it) -> {
                        return it.getName();
                      };
                      int _indexOf_22 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_45).indexOf(nextAction_6.getAgent().getName());
                      int _plus_77 = (_indexOf_22 + 1);
                      update.append(String.format("robot_to_be_followed[R-1] = %d,\n", Integer.valueOf(_plus_77)));
                      update.append("human_to_be_followed[R-1] = 0");
                    }
                  }
                  boolean _equals_26 = nextAction_6.getActionType().toString().equals("pick");
                  if (_equals_26) {
                    update.append(String.format("robot_res_to_pick[R-1] = %d, old_inventory = robot_inventory", Integer.valueOf(((List<String>)Conversions.doWrapArray(resources)).indexOf(nextAction_6.getResource().getName().toString()))));
                  }
                  boolean _equals_27 = nextAction_6.getActionType().toString().equals("deposit");
                  if (_equals_27) {
                    update.append(String.format("robot_res_to_deposit[R-1] = %d, old_inventory = robot_inventory", Integer.valueOf(((List<String>)Conversions.doWrapArray(resources)).indexOf(nextAction_6.getResource().getName().toString()))));
                  }
                }
                int _get_75 = firstActionID[subSequence.getI()];
                int _get_76 = actionCount[0];
                int _multiply_44 = (_get_76 * 2);
                int _plus_78 = (_get_75 + _multiply_44);
                int _get_77 = firstActionID[subSequence.getI()];
                int _get_78 = actionCount[0];
                int _multiply_45 = (_get_78 * 2);
                int _plus_79 = (_get_77 + _multiply_45);
                int _plus_80 = (_plus_79 + 1);
                String _string_20 = guard.toString();
                String _format_20 = String.format("stop_r_moveTo[R-1]!");
                String _string_21 = update.toString();
                int _get_79 = actionCount[0];
                int _multiply_46 = (_get_79 * 2);
                int _plus_81 = (_multiply_46 + 1);
                int _multiply_47 = (xOffset * _plus_81);
                int _i_40 = subSequence.getI();
                int _multiply_48 = (_i_40 * yOffset);
                int _minus_24 = (_multiply_48 - 100);
                transition.append(this.genTran(id[0], _plus_78, _plus_80, null, _string_20, _format_20, _string_21, true, _multiply_47, _minus_24, 0, 0));
                int _get_80 = id[0];
                int _plus_82 = (_get_80 + 1);
                id[0] = _plus_82;
                guard.setLength(0);
                update.setLength(0);
              }
            }
            String _string_22 = actionC.getActionType().toString();
            boolean _equals_28 = Objects.equals(_string_22, "follow");
            if (_equals_28) {
              final Function1<Robot, String> _function_46 = (Robot it) -> {
                return it.getName();
              };
              boolean _contains_23 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_46).contains(sequence.getAgent().getName());
              if (_contains_23) {
                int _get_81 = firstActionID[subSequence.getI()];
                int _get_82 = actionCount[0];
                int _multiply_49 = (_get_82 * 2);
                int _plus_83 = (_get_81 + _multiply_49);
                int _minus_25 = (_plus_83 - 1);
                int _get_83 = firstActionID[subSequence.getI()];
                int _get_84 = actionCount[0];
                int _multiply_50 = (_get_84 * 2);
                int _plus_84 = (_get_83 + _multiply_50);
                String _format_21 = String.format("start_r_follow[R-1]!");
                int _get_85 = actionCount[0];
                int _multiply_51 = (xOffset * _get_85);
                int _multiply_52 = (_multiply_51 * 2);
                int _i_41 = subSequence.getI();
                int _multiply_53 = (_i_41 * yOffset);
                transition.append(this.genTran(id[0], _minus_25, _plus_84, null, null, _format_21, null, true, _multiply_52, _multiply_53, 0, 0));
                int _get_86 = id[0];
                int _plus_85 = (_get_86 + 1);
                id[0] = _plus_85;
                final Function1<Human, String> _function_47 = (Human it) -> {
                  return it.getName();
                };
                boolean _contains_24 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_47).contains(actionC.getAgent().getName());
                if (_contains_24) {
                  guard.append(rMapGuard.get("followH"));
                }
                final Function1<Robot, String> _function_48 = (Robot it) -> {
                  return it.getName();
                };
                boolean _contains_25 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_48).contains(actionC.getAgent().getName());
                if (_contains_25) {
                  guard.append(rMapGuard.get("followR"));
                }
                int _indexOf_23 = subSequence.getActionC().indexOf(actionC);
                int _size_7 = subSequence.getActionC().size();
                int _minus_26 = (_size_7 - 1);
                boolean _lessThan_4 = (_indexOf_23 < _minus_26);
                if (_lessThan_4) {
                  EList<ActionC> _actionC_7 = subSequence.getActionC();
                  int _indexOf_24 = subSequence.getActionC().indexOf(actionC);
                  int _plus_86 = (_indexOf_24 + 1);
                  ActionC nextAction_7 = _actionC_7.get(_plus_86);
                  boolean _equals_29 = nextAction_7.getActionType().toString().equals("moveTo");
                  if (_equals_29) {
                    String nextTargetName_7 = nextAction_7.getTarget().getName().toString();
                    int nextTargetIndex_7 = (-1);
                    int nextLocIndex_7 = ((List<String>)Conversions.doWrapArray(locations)).indexOf(nextTargetName_7);
                    if ((nextLocIndex_7 != (-1))) {
                      nextTargetIndex_7 = nextLocIndex_7;
                    } else {
                      int nextResIndex_7 = ((List<String>)Conversions.doWrapArray(resources)).indexOf(nextTargetName_7);
                      if ((nextResIndex_7 != (-1))) {
                        int _length_9 = locations.length;
                        int _plus_87 = (_length_9 + nextResIndex_7);
                        nextTargetIndex_7 = _plus_87;
                      }
                    }
                    if ((nextTargetIndex_7 != (-1))) {
                      update.append(String.format("robot_loc_target[R-1] = %d", Integer.valueOf(nextTargetIndex_7)));
                    }
                  }
                  boolean _equals_30 = nextAction_7.getActionType().toString().equals("follow");
                  if (_equals_30) {
                    update.append(String.format("f_loc_target[R-1] = %d,\n", Integer.valueOf(((List<String>)Conversions.doWrapArray(locations)).indexOf(nextAction_7.getTarget().getName().toString()))));
                    final Function1<Human, String> _function_49 = (Human it) -> {
                      return it.getName();
                    };
                    boolean _contains_26 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_49).contains(nextAction_7.getAgent().getName());
                    if (_contains_26) {
                      final Function1<Human, String> _function_50 = (Human it) -> {
                        return it.getName();
                      };
                      int _indexOf_25 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_50).indexOf(nextAction_7.getAgent().getName());
                      int _plus_88 = (_indexOf_25 + 1);
                      update.append(String.format("human_to_be_followed[R-1] = %d,\n", Integer.valueOf(_plus_88)));
                      update.append("robot_to_be_followed[R-1] = 0");
                    }
                    final Function1<Robot, String> _function_51 = (Robot it) -> {
                      return it.getName();
                    };
                    boolean _contains_27 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_51).contains(nextAction_7.getAgent().getName());
                    if (_contains_27) {
                      final Function1<Robot, String> _function_52 = (Robot it) -> {
                        return it.getName();
                      };
                      int _indexOf_26 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_52).indexOf(nextAction_7.getAgent().getName());
                      int _plus_89 = (_indexOf_26 + 1);
                      update.append(String.format("robot_to_be_followed[R-1] = %d,\n", Integer.valueOf(_plus_89)));
                      update.append("human_to_be_followed[R-1] = 0");
                    }
                  }
                  boolean _equals_31 = nextAction_7.getActionType().toString().equals("pick");
                  if (_equals_31) {
                    update.append(String.format("robot_res_to_pick[R-1] = %d, old_inventory = robot_inventory", Integer.valueOf(((List<String>)Conversions.doWrapArray(resources)).indexOf(nextAction_7.getResource().getName().toString()))));
                  }
                  boolean _equals_32 = nextAction_7.getActionType().toString().equals("deposit");
                  if (_equals_32) {
                    update.append(String.format("robot_res_to_deposit[R-1] = %d, old_inventory = robot_inventory", Integer.valueOf(((List<String>)Conversions.doWrapArray(resources)).indexOf(nextAction_7.getResource().getName().toString()))));
                  }
                }
                int _get_87 = firstActionID[subSequence.getI()];
                int _get_88 = actionCount[0];
                int _multiply_54 = (_get_88 * 2);
                int _plus_90 = (_get_87 + _multiply_54);
                int _get_89 = firstActionID[subSequence.getI()];
                int _get_90 = actionCount[0];
                int _multiply_55 = (_get_90 * 2);
                int _plus_91 = (_get_89 + _multiply_55);
                int _plus_92 = (_plus_91 + 1);
                String _string_23 = guard.toString();
                String _format_22 = String.format("stop_r_follow[R-1]!");
                String _string_24 = update.toString();
                int _get_91 = actionCount[0];
                int _multiply_56 = (_get_91 * 2);
                int _plus_93 = (_multiply_56 + 1);
                int _multiply_57 = (xOffset * _plus_93);
                int _i_42 = subSequence.getI();
                int _multiply_58 = (_i_42 * yOffset);
                int _minus_27 = (_multiply_58 - 100);
                transition.append(this.genTran(id[0], _plus_90, _plus_92, null, _string_23, _format_22, _string_24, true, _multiply_57, _minus_27, 0, 0));
                int _get_92 = id[0];
                int _plus_94 = (_get_92 + 1);
                id[0] = _plus_94;
                guard.setLength(0);
                update.setLength(0);
              }
            }
            String _string_25 = actionC.getActionType().toString();
            boolean _equals_33 = Objects.equals(_string_25, "pick");
            if (_equals_33) {
              final Function1<Robot, String> _function_53 = (Robot it) -> {
                return it.getName();
              };
              boolean _contains_28 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_53).contains(sequence.getAgent().getName());
              if (_contains_28) {
                int _get_93 = firstActionID[subSequence.getI()];
                int _get_94 = actionCount[0];
                int _multiply_59 = (_get_94 * 2);
                int _plus_95 = (_get_93 + _multiply_59);
                int _minus_28 = (_plus_95 - 1);
                int _get_95 = firstActionID[subSequence.getI()];
                int _get_96 = actionCount[0];
                int _multiply_60 = (_get_96 * 2);
                int _plus_96 = (_get_95 + _multiply_60);
                String _format_23 = String.format("start_r_pick[R-1]!");
                int _get_97 = actionCount[0];
                int _multiply_61 = (xOffset * _get_97);
                int _multiply_62 = (_multiply_61 * 2);
                int _i_43 = subSequence.getI();
                int _multiply_63 = (_i_43 * yOffset);
                transition.append(this.genTran(id[0], _minus_28, _plus_96, null, null, _format_23, null, true, _multiply_62, _multiply_63, 0, 0));
                int _get_98 = id[0];
                int _plus_97 = (_get_98 + 1);
                id[0] = _plus_97;
                guard.append(rMapGuard.get("pick"));
                int _indexOf_27 = subSequence.getActionC().indexOf(actionC);
                int _size_8 = subSequence.getActionC().size();
                int _minus_29 = (_size_8 - 1);
                boolean _lessThan_5 = (_indexOf_27 < _minus_29);
                if (_lessThan_5) {
                  EList<ActionC> _actionC_8 = subSequence.getActionC();
                  int _indexOf_28 = subSequence.getActionC().indexOf(actionC);
                  int _plus_98 = (_indexOf_28 + 1);
                  ActionC nextAction_8 = _actionC_8.get(_plus_98);
                  boolean _equals_34 = nextAction_8.getActionType().toString().equals("moveTo");
                  if (_equals_34) {
                    String nextTargetName_8 = nextAction_8.getTarget().getName().toString();
                    int nextTargetIndex_8 = (-1);
                    int nextLocIndex_8 = ((List<String>)Conversions.doWrapArray(locations)).indexOf(nextTargetName_8);
                    if ((nextLocIndex_8 != (-1))) {
                      nextTargetIndex_8 = nextLocIndex_8;
                    } else {
                      int nextResIndex_8 = ((List<String>)Conversions.doWrapArray(resources)).indexOf(nextTargetName_8);
                      if ((nextResIndex_8 != (-1))) {
                        int _length_10 = locations.length;
                        int _plus_99 = (_length_10 + nextResIndex_8);
                        nextTargetIndex_8 = _plus_99;
                      }
                    }
                    if ((nextTargetIndex_8 != (-1))) {
                      update.append(String.format("robot_loc_target[R-1] = %d", Integer.valueOf(nextTargetIndex_8)));
                    }
                  }
                  boolean _equals_35 = nextAction_8.getActionType().toString().equals("follow");
                  if (_equals_35) {
                    update.append(String.format("f_loc_target[R-1] = %d,\n", Integer.valueOf(((List<String>)Conversions.doWrapArray(locations)).indexOf(nextAction_8.getTarget().getName().toString()))));
                    final Function1<Human, String> _function_54 = (Human it) -> {
                      return it.getName();
                    };
                    boolean _contains_29 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_54).contains(nextAction_8.getAgent().getName());
                    if (_contains_29) {
                      final Function1<Human, String> _function_55 = (Human it) -> {
                        return it.getName();
                      };
                      int _indexOf_29 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_55).indexOf(nextAction_8.getAgent().getName());
                      int _plus_100 = (_indexOf_29 + 1);
                      update.append(String.format("human_to_be_followed[R-1] = %d,\n", Integer.valueOf(_plus_100)));
                      update.append("robot_to_be_followed[R-1] = 0");
                    }
                    final Function1<Robot, String> _function_56 = (Robot it) -> {
                      return it.getName();
                    };
                    boolean _contains_30 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_56).contains(nextAction_8.getAgent().getName());
                    if (_contains_30) {
                      final Function1<Robot, String> _function_57 = (Robot it) -> {
                        return it.getName();
                      };
                      int _indexOf_30 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_57).indexOf(nextAction_8.getAgent().getName());
                      int _plus_101 = (_indexOf_30 + 1);
                      update.append(String.format("robot_to_be_followed[R-1] = %d,\n", Integer.valueOf(_plus_101)));
                      update.append("human_to_be_followed[R-1] = 0");
                    }
                  }
                  boolean _equals_36 = nextAction_8.getActionType().toString().equals("pick");
                  if (_equals_36) {
                    update.append(String.format("robot_res_to_pick[R-1] = %d, old_inventory = robot_inventory", Integer.valueOf(((List<String>)Conversions.doWrapArray(resources)).indexOf(nextAction_8.getResource().getName().toString()))));
                  }
                  boolean _equals_37 = nextAction_8.getActionType().toString().equals("deposit");
                  if (_equals_37) {
                    update.append(String.format("robot_res_to_deposit[R-1] = %d, old_inventory = robot_inventory", Integer.valueOf(((List<String>)Conversions.doWrapArray(resources)).indexOf(nextAction_8.getResource().getName().toString()))));
                  }
                }
                int _get_99 = firstActionID[subSequence.getI()];
                int _get_100 = actionCount[0];
                int _multiply_64 = (_get_100 * 2);
                int _plus_102 = (_get_99 + _multiply_64);
                int _get_101 = firstActionID[subSequence.getI()];
                int _get_102 = actionCount[0];
                int _multiply_65 = (_get_102 * 2);
                int _plus_103 = (_get_101 + _multiply_65);
                int _plus_104 = (_plus_103 + 1);
                String _string_26 = guard.toString();
                String _format_24 = String.format("stop_r_pick[R-1]!");
                String _string_27 = update.toString();
                int _get_103 = actionCount[0];
                int _multiply_66 = (_get_103 * 2);
                int _plus_105 = (_multiply_66 + 1);
                int _multiply_67 = (xOffset * _plus_105);
                int _i_44 = subSequence.getI();
                int _multiply_68 = (_i_44 * yOffset);
                int _minus_30 = (_multiply_68 - 100);
                transition.append(this.genTran(id[0], _plus_102, _plus_104, null, _string_26, _format_24, _string_27, true, _multiply_67, _minus_30, 0, 0));
                int _get_104 = id[0];
                int _plus_106 = (_get_104 + 1);
                id[0] = _plus_106;
                guard.setLength(0);
                update.setLength(0);
              }
            }
            String _string_28 = actionC.getActionType().toString();
            boolean _equals_38 = Objects.equals(_string_28, "deposit");
            if (_equals_38) {
              final Function1<Robot, String> _function_58 = (Robot it) -> {
                return it.getName();
              };
              boolean _contains_31 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_58).contains(sequence.getAgent().getName());
              if (_contains_31) {
                int _get_105 = firstActionID[subSequence.getI()];
                int _get_106 = actionCount[0];
                int _multiply_69 = (_get_106 * 2);
                int _plus_107 = (_get_105 + _multiply_69);
                int _minus_31 = (_plus_107 - 1);
                int _get_107 = firstActionID[subSequence.getI()];
                int _get_108 = actionCount[0];
                int _multiply_70 = (_get_108 * 2);
                int _plus_108 = (_get_107 + _multiply_70);
                String _format_25 = String.format("start_r_deposit[R-1]!");
                int _get_109 = actionCount[0];
                int _multiply_71 = (xOffset * _get_109);
                int _multiply_72 = (_multiply_71 * 2);
                int _i_45 = subSequence.getI();
                int _multiply_73 = (_i_45 * yOffset);
                transition.append(this.genTran(id[0], _minus_31, _plus_108, null, null, _format_25, null, true, _multiply_72, _multiply_73, 0, 0));
                int _get_110 = id[0];
                int _plus_109 = (_get_110 + 1);
                id[0] = _plus_109;
                guard.append(rMapGuard.get("deposit"));
                int _indexOf_31 = subSequence.getActionC().indexOf(actionC);
                int _size_9 = subSequence.getActionC().size();
                int _minus_32 = (_size_9 - 1);
                boolean _lessThan_6 = (_indexOf_31 < _minus_32);
                if (_lessThan_6) {
                  EList<ActionC> _actionC_9 = subSequence.getActionC();
                  int _indexOf_32 = subSequence.getActionC().indexOf(actionC);
                  int _plus_110 = (_indexOf_32 + 1);
                  ActionC nextAction_9 = _actionC_9.get(_plus_110);
                  boolean _equals_39 = nextAction_9.getActionType().toString().equals("moveTo");
                  if (_equals_39) {
                    String nextTargetName_9 = nextAction_9.getTarget().getName().toString();
                    int nextTargetIndex_9 = (-1);
                    int nextLocIndex_9 = ((List<String>)Conversions.doWrapArray(locations)).indexOf(nextTargetName_9);
                    if ((nextLocIndex_9 != (-1))) {
                      nextTargetIndex_9 = nextLocIndex_9;
                    } else {
                      int nextResIndex_9 = ((List<String>)Conversions.doWrapArray(resources)).indexOf(nextTargetName_9);
                      if ((nextResIndex_9 != (-1))) {
                        int _length_11 = locations.length;
                        int _plus_111 = (_length_11 + nextResIndex_9);
                        nextTargetIndex_9 = _plus_111;
                      }
                    }
                    if ((nextTargetIndex_9 != (-1))) {
                      update.append(String.format("robot_loc_target[R-1] = %d", Integer.valueOf(nextTargetIndex_9)));
                    }
                  }
                  boolean _equals_40 = nextAction_9.getActionType().toString().equals("follow");
                  if (_equals_40) {
                    update.append(String.format("f_loc_target[R-1] = %d,\n", Integer.valueOf(((List<String>)Conversions.doWrapArray(locations)).indexOf(nextAction_9.getTarget().getName().toString()))));
                    final Function1<Human, String> _function_59 = (Human it) -> {
                      return it.getName();
                    };
                    boolean _contains_32 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_59).contains(nextAction_9.getAgent().getName());
                    if (_contains_32) {
                      final Function1<Human, String> _function_60 = (Human it) -> {
                        return it.getName();
                      };
                      int _indexOf_33 = ListExtensions.<Human, String>map(model.getPattern().getHuman(), _function_60).indexOf(nextAction_9.getAgent().getName());
                      int _plus_112 = (_indexOf_33 + 1);
                      update.append(String.format("human_to_be_followed[R-1] = %d,\n", Integer.valueOf(_plus_112)));
                      update.append("robot_to_be_followed[R-1] = 0");
                    }
                    final Function1<Robot, String> _function_61 = (Robot it) -> {
                      return it.getName();
                    };
                    boolean _contains_33 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_61).contains(nextAction_9.getAgent().getName());
                    if (_contains_33) {
                      final Function1<Robot, String> _function_62 = (Robot it) -> {
                        return it.getName();
                      };
                      int _indexOf_34 = ListExtensions.<Robot, String>map(model.getPattern().getRobot(), _function_62).indexOf(nextAction_9.getAgent().getName());
                      int _plus_113 = (_indexOf_34 + 1);
                      update.append(String.format("robot_to_be_followed[R-1] = %d,\n", Integer.valueOf(_plus_113)));
                      update.append("human_to_be_followed[R-1] = 0");
                    }
                  }
                  boolean _equals_41 = nextAction_9.getActionType().toString().equals("pick");
                  if (_equals_41) {
                    update.append(String.format("robot_res_to_pick[R-1] = %d, old_inventory = robot_inventory", Integer.valueOf(((List<String>)Conversions.doWrapArray(resources)).indexOf(nextAction_9.getResource().getName().toString()))));
                  }
                  boolean _equals_42 = nextAction_9.getActionType().toString().equals("deposit");
                  if (_equals_42) {
                    update.append(String.format("robot_res_to_deposit[R-1] = %d, old_inventory = robot_inventory", Integer.valueOf(((List<String>)Conversions.doWrapArray(resources)).indexOf(nextAction_9.getResource().getName().toString()))));
                  }
                }
                int _get_111 = firstActionID[subSequence.getI()];
                int _get_112 = actionCount[0];
                int _multiply_74 = (_get_112 * 2);
                int _plus_114 = (_get_111 + _multiply_74);
                int _get_113 = firstActionID[subSequence.getI()];
                int _get_114 = actionCount[0];
                int _multiply_75 = (_get_114 * 2);
                int _plus_115 = (_get_113 + _multiply_75);
                int _plus_116 = (_plus_115 + 1);
                String _string_29 = guard.toString();
                String _format_26 = String.format("stop_r_deposit[R-1]!");
                String _string_30 = update.toString();
                int _get_115 = actionCount[0];
                int _multiply_76 = (_get_115 * 2);
                int _plus_117 = (_multiply_76 + 1);
                int _multiply_77 = (xOffset * _plus_117);
                int _i_46 = subSequence.getI();
                int _multiply_78 = (_i_46 * yOffset);
                int _minus_33 = (_multiply_78 - 100);
                transition.append(this.genTran(id[0], _plus_114, _plus_116, null, _string_29, _format_26, _string_30, true, _multiply_77, _minus_33, 0, 0));
                int _get_116 = id[0];
                int _plus_118 = (_get_116 + 1);
                id[0] = _plus_118;
                guard.setLength(0);
                update.setLength(0);
              }
            }
          }
        };
        subSequence.getActionC().forEach(_function_12);
        actionCount[0] = 0;
      };
      sequence.getSubSequence().forEach(_function_11);
      int _get_7 = maxIndex[0];
      IntegerRange _upTo_2 = new IntegerRange(1, _get_7);
      for (final Integer i_2 : _upTo_2) {
        {
          int _get_8 = emptySS[((i_2).intValue() - 1)];
          boolean _equals = (_get_8 == 0);
          if (_equals) {
            x[0] = xOffset;
            y[0] = ((i_2).intValue() * yOffset);
            transition.append(this.genTran(id[0], endSubSeqID[((i_2).intValue() - 1)], (i_2).intValue(), null, null, String.format("s%d?", i_2), null, true, 0, (((i_2).intValue() * yOffset) - 100), 0, 0));
            int _get_9 = id[0];
            int _plus_5 = (_get_9 + 1);
            id[0] = _plus_5;
            transition.append(this.genTran(id[0], (i_2).intValue(), endSubSeqID[(i_2).intValue()], null, null, null, String.format("%s_stat = true", sequence.getAgent().getName().toLowerCase()), true, 0, ((i_2).intValue() * yOffset), 0, 0));
            int _get_10 = id[0];
            int _plus_6 = (_get_10 + 1);
            id[0] = _plus_6;
          }
          emptySS[((i_2).intValue() - 1)] = 0;
        }
      }
      String _format = String.format("e?");
      int _get_8 = maxIndex[0];
      int _plus_5 = (_get_8 + 1);
      int _multiply = (_plus_5 * yOffset);
      int _minus = (_multiply - 100);
      transition.append(this.genTran(id[0], endSubSeqID[maxIndex[0]], endID[0], null, null, _format, null, true, 0, _minus, 0, 0));
      int _get_9 = id[0];
      int _plus_6 = (_get_9 + 1);
      id[0] = _plus_6;
      builder.append(transition.toString());
      transition.delete(0, transition.length());
      builder.append("\t</template>\n");
    };
    model.getSequence().forEach(_function_5);
    return builder.toString();
  }

  public String apPositionGuard(final Model model, final String Agent, final String Location) {
    final int[] locNum = { 0 };
    final int[] hNum = { 0 };
    final int[] rNum = { 0 };
    final StringBuilder builder = new StringBuilder();
    final Consumer<it.polimi.lIRAs.Location> _function = (it.polimi.lIRAs.Location loc) -> {
      int _get = locNum[0];
      int _plus = (_get + 1);
      locNum[0] = _plus;
    };
    model.getPattern().getLocation().forEach(_function);
    final String[] locations = new String[locNum[0]];
    locNum[0] = 0;
    final Consumer<it.polimi.lIRAs.Location> _function_1 = (it.polimi.lIRAs.Location loc) -> {
      locations[locNum[0]] = loc.getName().toString();
      int _get = locNum[0];
      int _plus = (_get + 1);
      locNum[0] = _plus;
    };
    model.getPattern().getLocation().forEach(_function_1);
    final Consumer<Human> _function_2 = (Human hum) -> {
      int _get = hNum[0];
      int _plus = (_get + 1);
      hNum[0] = _plus;
    };
    model.getPattern().getHuman().forEach(_function_2);
    final String[] humans = new String[hNum[0]];
    hNum[0] = 0;
    final Consumer<Human> _function_3 = (Human hum) -> {
      humans[hNum[0]] = hum.getName().toString();
      int _get = hNum[0];
      int _plus = (_get + 1);
      hNum[0] = _plus;
    };
    model.getPattern().getHuman().forEach(_function_3);
    final Consumer<Robot> _function_4 = (Robot rob) -> {
      int _get = rNum[0];
      int _plus = (_get + 1);
      rNum[0] = _plus;
    };
    model.getPattern().getRobot().forEach(_function_4);
    final String[] robots = new String[rNum[0]];
    rNum[0] = 0;
    final Consumer<Robot> _function_5 = (Robot rob) -> {
      robots[rNum[0]] = rob.getName().toString();
      int _get = rNum[0];
      int _plus = (_get + 1);
      rNum[0] = _plus;
    };
    model.getPattern().getRobot().forEach(_function_5);
    final int locIndex = ((List<String>)Conversions.doWrapArray(locations)).indexOf(Location);
    boolean _contains = ArrayExtensions.contains(humans, Agent);
    if (_contains) {
      final int hIndex = ((List<String>)Conversions.doWrapArray(humans)).indexOf(Agent);
      builder.append(String.format((((((((((((((((("humanPositionX[" + Integer.valueOf(hIndex)) + "] &lt;= location_X[") + Integer.valueOf(locIndex)) + "]+30 and\nhumanPositionX[") + Integer.valueOf(hIndex)) + "] &gt;= location_X[") + Integer.valueOf(locIndex)) + "]-30 and\nhumanPositionY[") + Integer.valueOf(hIndex)) + "] &lt;= location_Y[") + Integer.valueOf(locIndex)) + "]+30 and\nhumanPositionY[") + Integer.valueOf(hIndex)) + "] &gt;= location_Y[") + Integer.valueOf(locIndex)) + "]-30")));
    }
    boolean _contains_1 = ArrayExtensions.contains(robots, Agent);
    if (_contains_1) {
      final int rIndex = ((List<String>)Conversions.doWrapArray(robots)).indexOf(Agent);
      builder.append(String.format((((((((((((((((("robPositionX[" + Integer.valueOf(rIndex)) + "] &lt;= location_X[") + Integer.valueOf(locIndex)) + "]+30 and\nrobPositionX[") + Integer.valueOf(rIndex)) + "] &gt;= location_X[") + Integer.valueOf(locIndex)) + "]-30 and\nrobPositionY[") + Integer.valueOf(rIndex)) + "] &lt;= location_Y[") + Integer.valueOf(locIndex)) + "]+30 and\nrobPositionY[") + Integer.valueOf(rIndex)) + "] &gt;= location_Y[") + Integer.valueOf(locIndex)) + "]-30")));
    }
    return builder.toString();
  }

  public String genLoc(final int id, final int x, final int y, final String name, final String invariant, final String Rate_of_exp, final boolean urgent, final boolean committed) {
    final StringBuilder builder = new StringBuilder();
    builder.append(String.format("\t\t<location id=\"id%d\" x=\"%d\" y=\"%d\">\n", Integer.valueOf(id), Integer.valueOf(x), Integer.valueOf(y)));
    if ((name != null)) {
      builder.append(String.format("\t\t\t<name x=\"%d\" y=\"%d\">%s</name>\n", Integer.valueOf((x - 17)), Integer.valueOf((y - 34)), name));
    }
    if ((invariant != null)) {
      builder.append(String.format("\t\t\t<label kind=\"invariant\" x=\"%d\" y=\"%d\">%s</label>\n", Integer.valueOf((x - 10)), Integer.valueOf((y + 17)), invariant));
    }
    if ((Rate_of_exp != null)) {
      builder.append(String.format("\t\t\t<label kind=\"exponentialrate\" x=\"%d\" y=\"%d\">%s</label>\n", Integer.valueOf((x - 10)), Integer.valueOf((y + 34)), Rate_of_exp));
    }
    if (((Boolean.valueOf(urgent) != Boolean.valueOf(false)) && (Boolean.valueOf(committed) != Boolean.valueOf(true)))) {
      builder.append(String.format("\t\t\t<urgent/>\n", Integer.valueOf(id)));
    }
    if (((Boolean.valueOf(committed) != Boolean.valueOf(false)) && (Boolean.valueOf(urgent) != Boolean.valueOf(true)))) {
      builder.append(String.format("\t\t\t<committed/>\n", Integer.valueOf(id)));
    }
    builder.append("\t\t</location>\n");
    return builder.toString();
  }

  public String genTran(final int id, final int source, final int target, final String select, final String guard, final String sync, final String update, final boolean controllable, final int xOffset, final int yOffset, final int nailX, final int nailY) {
    final StringBuilder builder = new StringBuilder();
    if ((controllable == false)) {
      builder.append(String.format("\t\t<transition id=\"id%d\" controllable=\"false\">\n", Integer.valueOf(id)));
    } else {
      builder.append(String.format("\t\t<transition id=\"id%d\">\n", Integer.valueOf(id)));
    }
    builder.append(String.format("\t\t\t<source ref=\"id%d\"/>\n", Integer.valueOf(source)));
    builder.append(String.format("\t\t\t<target ref=\"id%d\"/>\n", Integer.valueOf(target)));
    if (((xOffset == 500) && (yOffset == 0))) {
      if ((select != null)) {
        builder.append(String.format("\t\t\t<label kind=\"select\" x=\"%d\" y=\"%d\">%s</label>\n", Integer.valueOf((17 + (xOffset * source))), Integer.valueOf(((-51) + (yOffset * source))), select));
      }
      if ((guard != null)) {
        builder.append(String.format("\t\t\t<label kind=\"guard\" x=\"%d\" y=\"%d\">%s</label>\n", Integer.valueOf((18 + (xOffset * source))), Integer.valueOf(((-34) + (yOffset * source))), guard));
      }
      if ((sync != null)) {
        builder.append(String.format("\t\t\t<label kind=\"synchronisation\" x=\"%d\" y=\"%d\">%s</label>\n", Integer.valueOf((18 + (xOffset * source))), Integer.valueOf(((-17) + (yOffset * source))), sync));
      }
      if ((update != null)) {
        builder.append(String.format("\t\t\t<label kind=\"assignment\" x=\"%d\" y=\"%d\">%s</label>\n", Integer.valueOf((18 + (xOffset * source))), Integer.valueOf((0 + (yOffset * source))), update));
      }
      if (((nailX != 0) && (nailY != 0))) {
        builder.append(String.format("\t\t\t<nail x=\"%d\" y=\"%d\"/>\n", Integer.valueOf(nailX), Integer.valueOf(nailY)));
      }
    } else {
      if ((select != null)) {
        builder.append(String.format("\t\t\t<label kind=\"select\" x=\"%d\" y=\"%d\">%s</label>\n", Integer.valueOf((17 + xOffset)), Integer.valueOf(((-51) + yOffset)), select));
      }
      if ((guard != null)) {
        builder.append(String.format("\t\t\t<label kind=\"guard\" x=\"%d\" y=\"%d\">%s</label>\n", Integer.valueOf((18 + xOffset)), Integer.valueOf(((-34) + yOffset)), guard));
      }
      if ((sync != null)) {
        builder.append(String.format("\t\t\t<label kind=\"synchronisation\" x=\"%d\" y=\"%d\">%s</label>\n", Integer.valueOf((18 + xOffset)), Integer.valueOf(((-17) + yOffset)), sync));
      }
      if ((update != null)) {
        builder.append(String.format("\t\t\t<label kind=\"assignment\" x=\"%d\" y=\"%d\">%s</label>\n", Integer.valueOf((18 + xOffset)), Integer.valueOf((0 + yOffset)), update));
      }
      if (((nailX != 0) && (nailY != 0))) {
        builder.append(String.format("\t\t\t<nail x=\"%d\" y=\"%d\"/>\n", Integer.valueOf(nailX), Integer.valueOf(nailY)));
      }
    }
    builder.append("\t\t</transition>\n");
    return builder.toString();
  }

  public String verifier(final Model model) {
    final int simTime = 700;
    final Pattern pattern = model.getPattern();
    final String patternName = pattern.getName().toString();
    final Function1<Sequence, EList<SubSequence>> _function = (Sequence it) -> {
      return it.getSubSequence();
    };
    final Iterable<SubSequence> allSS = IterableExtensions.<Sequence, SubSequence>flatMap(model.getSequence(), _function);
    Integer _elvis = null;
    final Function1<SubSequence, Integer> _function_1 = (SubSequence it) -> {
      return Integer.valueOf(Math.max(it.getI(), it.getJ()));
    };
    Integer _max = IterableExtensions.<Integer>max(IterableExtensions.<SubSequence, Integer>map(allSS, _function_1));
    if (_max != null) {
      _elvis = _max;
    } else {
      _elvis = Integer.valueOf(0);
    }
    final Integer maxIndex = _elvis;
    final ArrayList<String> queries = CollectionLiterals.<String>newArrayList();
    final Consumer<Sequence> _function_2 = (Sequence seq) -> {
      final int[] continuation = new int[(maxIndex).intValue()];
      final int[] emptySS = new int[(maxIndex).intValue()];
      final String agent = seq.getAgent().getName().toLowerCase();
      final Consumer<SubSequence> _function_3 = (SubSequence ss) -> {
        int _j = ss.getJ();
        int _i = ss.getI();
        boolean _greaterThan = (_j > _i);
        if (_greaterThan) {
          int _i_1 = ss.getI();
          int _plus = (_i_1 + 1);
          int _j_1 = ss.getJ();
          final Consumer<Integer> _function_4 = (Integer k) -> {
            continuation[((k).intValue() - 1)] = 1;
            emptySS[((k).intValue() - 1)] = 1;
          };
          new IntegerRange(_plus, _j_1).forEach(_function_4);
        }
        if (((ss.getActionC() != null) && (!ss.getActionC().isEmpty()))) {
          int _i_2 = ss.getI();
          int _minus = (_i_2 - 1);
          emptySS[_minus] = 1;
        }
      };
      seq.getSubSequence().forEach(_function_3);
      final Function1<Integer, String> _function_4 = (Integer i) -> {
        String _xblockexpression = null;
        {
          final int c = continuation[((i).intValue() - 1)];
          final int e = emptySS[((i).intValue() - 1)];
          String _xifexpression = null;
          if (((e == 0) && (c == 0))) {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append(patternName);
            _builder.append("_");
            _builder.append(agent);
            _builder.append("_def.S");
            _builder.append(i);
            _builder.append("_idle");
            _xifexpression = _builder.toString();
          } else {
            String _xifexpression_1 = null;
            if (((e == 1) && (c == 0))) {
              StringConcatenation _builder_1 = new StringConcatenation();
              _builder_1.append(patternName);
              _builder_1.append("_");
              _builder_1.append(agent);
              _builder_1.append("_def.s");
              _builder_1.append(i);
              _builder_1.append("_end");
              _xifexpression_1 = _builder_1.toString();
            }
            _xifexpression = _xifexpression_1;
          }
          _xblockexpression = _xifexpression;
        }
        return _xblockexpression;
      };
      final String states = IterableExtensions.join(IterableExtensions.<String>filterNull(IterableExtensions.<Integer, String>map(new IntegerRange(1, (maxIndex).intValue()), _function_4)), ", ");
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("<query>");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("<formula>simulate[&lt;=");
      _builder.append(simTime, "    ");
      _builder.append("]{");
      _builder.append(states, "    ");
      _builder.append(", ");
      _builder.append(patternName, "    ");
      _builder.append(".end}</formula>");
      _builder.newLineIfNotEmpty();
      _builder.append("</query>");
      _builder.newLine();
      queries.add(_builder.toString());
    };
    model.getSequence().forEach(_function_2);
    final int hCount = pattern.getHuman().size();
    final int rCount = pattern.getRobot().size();
    final int resCount = pattern.getResources().size();
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<query>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<formula>Pr[&lt;=");
    _builder.append(simTime, "    ");
    _builder.append("] (&lt;&gt; ");
    _builder.append(patternName, "    ");
    _builder.append(".end)</formula>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<comment>Check if the whole pattern ends within ");
    _builder.append(simTime, "    ");
    _builder.append(" time unit</comment>");
    _builder.newLineIfNotEmpty();
    _builder.append("</query>");
    _builder.newLine();
    queries.add(_builder.toString());
    final Function1<Integer, String> _function_3 = (Integer it) -> {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("humanPositionX[");
      _builder_1.append(it);
      _builder_1.append("], humanPositionY[");
      _builder_1.append(it);
      _builder_1.append("]");
      return _builder_1.toString();
    };
    Iterable<String> _map = IterableExtensions.<Integer, String>map(new ExclusiveRange(0, hCount, true), _function_3);
    final Function1<Integer, String> _function_4 = (Integer it) -> {
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
    Iterable<String> _map_1 = IterableExtensions.<Integer, String>map(new ExclusiveRange(0, rCount, true), _function_4);
    final Iterable<String> posParts = Iterables.<String>concat(_map, _map_1);
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("<query>");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("<formula>simulate[&lt;=");
    _builder_1.append(simTime, "    ");
    _builder_1.append("]{");
    String _join = IterableExtensions.join(posParts, ", ");
    _builder_1.append(_join, "    ");
    _builder_1.append(", ");
    _builder_1.append(patternName, "    ");
    _builder_1.append(".end}</formula>");
    _builder_1.newLineIfNotEmpty();
    _builder_1.append("    ");
    _builder_1.append("<comment>Check the position of all the agent in the pattern for ");
    _builder_1.append(simTime, "    ");
    _builder_1.append(" time unit</comment>");
    _builder_1.newLineIfNotEmpty();
    _builder_1.append("</query>");
    _builder_1.newLine();
    queries.add(_builder_1.toString());
    final Function1<Integer, Iterable<String>> _function_5 = (Integer r) -> {
      final Function1<Integer, String> _function_6 = (Integer res) -> {
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("robot_inventory[");
        _builder_2.append(r);
        _builder_2.append("][");
        _builder_2.append(res);
        _builder_2.append("]");
        return _builder_2.toString();
      };
      return IterableExtensions.<Integer, String>map(new ExclusiveRange(0, resCount, true), _function_6);
    };
    final String invs = IterableExtensions.join(IterableExtensions.<Integer, String>flatMap(new ExclusiveRange(0, rCount, true), _function_5), ", ");
    StringConcatenation _builder_2 = new StringConcatenation();
    _builder_2.append("<query>");
    _builder_2.newLine();
    _builder_2.append("    ");
    _builder_2.append("<formula>simulate[&lt;=");
    _builder_2.append(simTime, "    ");
    _builder_2.append("]{");
    _builder_2.append(invs, "    ");
    _builder_2.append(", ");
    _builder_2.append(patternName, "    ");
    _builder_2.append(".end*2}</formula>");
    _builder_2.newLineIfNotEmpty();
    _builder_2.append("    ");
    _builder_2.append("<comment>Check quantity of each item for each robot</comment>");
    _builder_2.newLine();
    _builder_2.append("</query>");
    _builder_2.newLine();
    queries.add(_builder_2.toString());
    final Function1<Location, String> _function_6 = (Location it) -> {
      String _name = it.getName();
      String _plus = (_name + "[0], ");
      String _name_1 = it.getName();
      String _plus_1 = (_plus + _name_1);
      return (_plus_1 + "[1]");
    };
    final String locs = IterableExtensions.join(ListExtensions.<Location, String>map(pattern.getLocation(), _function_6), ", ");
    StringConcatenation _builder_3 = new StringConcatenation();
    _builder_3.append("<query>");
    _builder_3.newLine();
    _builder_3.append("    ");
    _builder_3.append("<formula>simulate[&lt;=");
    _builder_3.append(simTime, "    ");
    _builder_3.append("]{");
    _builder_3.append(locs, "    ");
    _builder_3.append("}</formula>");
    _builder_3.newLineIfNotEmpty();
    _builder_3.append("    ");
    _builder_3.append("<comment>Plot the position of each location in the pattern</comment>");
    _builder_3.newLine();
    _builder_3.append("</query>");
    _builder_3.newLine();
    queries.add(_builder_3.toString());
    final Function1<Integer, String> _function_7 = (Integer i) -> {
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
    final String rmodes = IterableExtensions.join(IterableExtensions.<Integer, String>map(new IntegerRange(1, rCount), _function_7), ", ");
    StringConcatenation _builder_4 = new StringConcatenation();
    _builder_4.append("<query>");
    _builder_4.newLine();
    _builder_4.append("    ");
    _builder_4.append("<formula>simulate[&lt;=");
    _builder_4.append(simTime, "    ");
    _builder_4.append("]{");
    _builder_4.append(rmodes, "    ");
    _builder_4.append("}</formula>");
    _builder_4.newLineIfNotEmpty();
    _builder_4.append("    ");
    _builder_4.append("<comment></comment>");
    _builder_4.newLine();
    _builder_4.append("</query>");
    _builder_4.newLine();
    queries.add(_builder_4.toString());
    final Function1<Integer, String> _function_8 = (Integer i) -> {
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
    final String rmodes2 = IterableExtensions.join(IterableExtensions.<Integer, String>map(new IntegerRange(1, rCount), _function_8), " || ");
    StringConcatenation _builder_5 = new StringConcatenation();
    _builder_5.append("<query>");
    _builder_5.newLine();
    _builder_5.append("    ");
    _builder_5.append("<formula>Pr[&lt;=");
    _builder_5.append(simTime, "    ");
    _builder_5.append("](&lt;&gt;");
    _builder_5.append(rmodes2, "    ");
    _builder_5.append(")</formula>");
    _builder_5.newLineIfNotEmpty();
    _builder_5.append("    ");
    _builder_5.append("<comment></comment>");
    _builder_5.newLine();
    _builder_5.append("</query>");
    _builder_5.newLine();
    queries.add(_builder_5.toString());
    final Function1<Integer, String> _function_9 = (Integer i) -> {
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
    final String rmodes3 = IterableExtensions.join(IterableExtensions.<Integer, String>map(new IntegerRange(1, rCount), _function_9), " &amp;&amp; ");
    StringConcatenation _builder_6 = new StringConcatenation();
    _builder_6.append("<query>");
    _builder_6.newLine();
    _builder_6.append("    ");
    _builder_6.append("<formula>Pr[&lt;=");
    _builder_6.append(simTime, "    ");
    _builder_6.append("]([] (");
    _builder_6.append(patternName, "    ");
    _builder_6.append(".end imply (");
    _builder_6.append(rmodes3, "    ");
    _builder_6.append(")))</formula>");
    _builder_6.newLineIfNotEmpty();
    _builder_6.append("    ");
    _builder_6.append("<comment></comment>");
    _builder_6.newLine();
    _builder_6.append("</query>");
    _builder_6.newLine();
    queries.add(_builder_6.toString());
    final Function1<Robot, String> _function_10 = (Robot it) -> {
      return it.getName();
    };
    final List<String> robotList = ListExtensions.<Robot, String>map(pattern.getRobot(), _function_10);
    final Function1<Human, String> _function_11 = (Human it) -> {
      return it.getName();
    };
    final List<String> humanList = ListExtensions.<Human, String>map(pattern.getHuman(), _function_11);
    final Consumer<Sequence> _function_12 = (Sequence seq) -> {
      final int rIndex = robotList.indexOf(seq.getAgent().getName());
      final int hIndex = humanList.indexOf(seq.getAgent().getName());
      if ((rIndex != (-1))) {
        final HashSet<String> targets = new HashSet<String>();
        final Consumer<SubSequence> _function_13 = (SubSequence ss) -> {
          final Consumer<ActionC> _function_14 = (ActionC act) -> {
            String _actionType = act.getActionType();
            boolean _equals = Objects.equals(_actionType, "moveTo");
            if (_equals) {
              targets.add(act.getTarget().getName());
            }
          };
          ss.getActionC().forEach(_function_14);
        };
        seq.getSubSequence().forEach(_function_13);
        final Consumer<String> _function_14 = (String tName) -> {
          StringConcatenation _builder_7 = new StringConcatenation();
          _builder_7.append("<query>");
          _builder_7.newLine();
          _builder_7.append("    ");
          _builder_7.append("<formula>Pr[&lt;=");
          _builder_7.append(simTime, "    ");
          _builder_7.append("] (&lt;&gt; pow(robPositionX[");
          _builder_7.append(rIndex, "    ");
          _builder_7.append("] - ");
          _builder_7.append(tName, "    ");
          _builder_7.append("[0], 2) + pow(robPositionY[");
          _builder_7.append(rIndex, "    ");
          _builder_7.append("] - ");
          _builder_7.append(tName, "    ");
          _builder_7.append("[1], 2) &lt;= 900 )</formula>");
          _builder_7.newLineIfNotEmpty();
          _builder_7.append("    ");
          _builder_7.append("<comment>Check if robot ");
          String _name = seq.getAgent().getName();
          _builder_7.append(_name, "    ");
          _builder_7.append(" reaches ");
          _builder_7.append(tName, "    ");
          _builder_7.append(" (within 30 units)</comment>");
          _builder_7.newLineIfNotEmpty();
          _builder_7.append("</query>");
          _builder_7.newLine();
          queries.add(_builder_7.toString());
        };
        targets.forEach(_function_14);
      }
      if ((hIndex != (-1))) {
        final HashSet<String> targets_1 = new HashSet<String>();
        final Consumer<SubSequence> _function_15 = (SubSequence ss) -> {
          final Consumer<ActionC> _function_16 = (ActionC act) -> {
            String _actionType = act.getActionType();
            boolean _equals = Objects.equals(_actionType, "moveTo");
            if (_equals) {
              targets_1.add(act.getTarget().getName());
            }
          };
          ss.getActionC().forEach(_function_16);
        };
        seq.getSubSequence().forEach(_function_15);
        final Consumer<String> _function_16 = (String tName) -> {
          StringConcatenation _builder_7 = new StringConcatenation();
          _builder_7.append("<query>");
          _builder_7.newLine();
          _builder_7.append("    ");
          _builder_7.append("<formula>Pr[&lt;=");
          _builder_7.append(simTime, "    ");
          _builder_7.append("] (&lt;&gt; pow(humanPositionX[");
          _builder_7.append(rIndex, "    ");
          _builder_7.append("] - ");
          _builder_7.append(tName, "    ");
          _builder_7.append("[0], 2) + pow(humanPositionY[");
          _builder_7.append(rIndex, "    ");
          _builder_7.append("] - ");
          _builder_7.append(tName, "    ");
          _builder_7.append("[1], 2) &lt;= 900 )</formula>");
          _builder_7.newLineIfNotEmpty();
          _builder_7.append("    ");
          _builder_7.append("<comment>Check if human ");
          String _name = seq.getAgent().getName();
          _builder_7.append(_name, "    ");
          _builder_7.append(" reaches ");
          _builder_7.append(tName, "    ");
          _builder_7.append(" (within 30 units)</comment>");
          _builder_7.newLineIfNotEmpty();
          _builder_7.append("</query>");
          _builder_7.newLine();
          queries.add(_builder_7.toString());
        };
        targets_1.forEach(_function_16);
      }
    };
    model.getSequence().forEach(_function_12);
    final Procedure2<Human, Integer> _function_13 = (Human h, Integer i) -> {
      StringConcatenation _builder_7 = new StringConcatenation();
      _builder_7.append("<query>");
      _builder_7.newLine();
      _builder_7.append("    ");
      _builder_7.append("<formula>Pr[&lt;=");
      _builder_7.append(simTime, "    ");
      _builder_7.append("] (&lt;&gt; !isValidPosition(humanPositionX[");
      _builder_7.append(i, "    ");
      _builder_7.append("], humanPositionY[");
      _builder_7.append(i, "    ");
      _builder_7.append("]))</formula>");
      _builder_7.newLineIfNotEmpty();
      _builder_7.append("    ");
      _builder_7.append("<comment>Check if human ");
      String _name = h.getName();
      _builder_7.append(_name, "    ");
      _builder_7.append(" enters an invalid position (e.g. collision or obstacle)</comment>");
      _builder_7.newLineIfNotEmpty();
      _builder_7.append("</query>");
      _builder_7.newLine();
      queries.add(_builder_7.toString());
    };
    IterableExtensions.<Human>forEach(model.getPattern().getHuman(), _function_13);
    final Procedure2<Robot, Integer> _function_14 = (Robot r, Integer i) -> {
      StringConcatenation _builder_7 = new StringConcatenation();
      _builder_7.append("<query>");
      _builder_7.newLine();
      _builder_7.append("    ");
      _builder_7.append("<formula>Pr[&lt;=");
      _builder_7.append(simTime, "    ");
      _builder_7.append("] (&lt;&gt; !isValidPosition(robPositionX[");
      _builder_7.append(i, "    ");
      _builder_7.append("], robPositionY[");
      _builder_7.append(i, "    ");
      _builder_7.append("]))</formula>");
      _builder_7.newLineIfNotEmpty();
      _builder_7.append("    ");
      _builder_7.append("<comment>Check if robot ");
      String _name = r.getName();
      _builder_7.append(_name, "    ");
      _builder_7.append(" enters an invalid position (e.g. collision or obstacle)</comment>");
      _builder_7.newLineIfNotEmpty();
      _builder_7.append("</query>");
      _builder_7.newLine();
      queries.add(_builder_7.toString());
    };
    IterableExtensions.<Robot>forEach(model.getPattern().getRobot(), _function_14);
    StringConcatenation _builder_7 = new StringConcatenation();
    _builder_7.append("<query>");
    _builder_7.newLine();
    _builder_7.append("    ");
    _builder_7.append("<formula>Pr[&lt;=");
    _builder_7.append(simTime, "    ");
    _builder_7.append("] (&lt;&gt; currP > 2)</formula>");
    _builder_7.newLineIfNotEmpty();
    _builder_7.append("    ");
    _builder_7.append("<comment>Check if pattern is executed more then once</comment>");
    _builder_7.newLine();
    _builder_7.append("</query>");
    _builder_7.newLine();
    queries.add(_builder_7.toString());
    return IterableExtensions.join(queries, "\n");
  }
}
