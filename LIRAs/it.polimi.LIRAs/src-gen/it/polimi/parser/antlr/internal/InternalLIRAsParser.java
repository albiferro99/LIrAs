package it.polimi.parser.antlr.internal;

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import it.polimi.services.LIRAsGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalLIRAsParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_NL", "RULE_ID", "RULE_INT", "RULE_STRING", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'agents:'", "'humans:'", "','", "'robots:'", "'locations:'", "'resources:'", "'default'", "':'", "'-'", "'success:'", "'failure:'", "'skip'", "'all'", "'if'", "'restart'", "'until'", "'else'", "'wait'", "'stop'", "'moveTo'", "'fetch'", "'pick'", "'deliver'", "'leave'", "'follow'", "'('", "')'", "'receiveFrom'", "'deposit'", "'&&'", "'||'", "'!'", "'tired'", "'time'", "'position'", "'possess'", "'busy'", "'still'", "'dist'", "'>'", "'<'", "'>='", "'<='", "'=='", "'!='", "'soc'"
    };
    public static final int T__50=50;
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__55=55;
    public static final int T__12=12;
    public static final int T__56=56;
    public static final int T__13=13;
    public static final int T__57=57;
    public static final int T__14=14;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int RULE_ID=5;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int RULE_INT=6;
    public static final int T__29=29;
    public static final int T__22=22;
    public static final int RULE_ML_COMMENT=8;
    public static final int T__23=23;
    public static final int T__24=24;
    public static final int T__25=25;
    public static final int T__20=20;
    public static final int T__21=21;
    public static final int RULE_STRING=7;
    public static final int RULE_SL_COMMENT=9;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int T__33=33;
    public static final int T__34=34;
    public static final int T__35=35;
    public static final int T__36=36;
    public static final int EOF=-1;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int RULE_WS=10;
    public static final int RULE_ANY_OTHER=11;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int RULE_NL=4;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int T__42=42;
    public static final int T__43=43;

    // delegates
    // delegators


        public InternalLIRAsParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalLIRAsParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalLIRAsParser.tokenNames; }
    public String getGrammarFileName() { return "InternalLIRAs.g"; }



     	private LIRAsGrammarAccess grammarAccess;

        public InternalLIRAsParser(TokenStream input, LIRAsGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }

        @Override
        protected String getFirstRuleName() {
        	return "Model";
       	}

       	@Override
       	protected LIRAsGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}




    // $ANTLR start "entryRuleModel"
    // InternalLIRAs.g:64:1: entryRuleModel returns [EObject current=null] : iv_ruleModel= ruleModel EOF ;
    public final EObject entryRuleModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModel = null;


        try {
            // InternalLIRAs.g:64:46: (iv_ruleModel= ruleModel EOF )
            // InternalLIRAs.g:65:2: iv_ruleModel= ruleModel EOF
            {
             newCompositeNode(grammarAccess.getModelRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleModel=ruleModel();

            state._fsp--;

             current =iv_ruleModel; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleModel"


    // $ANTLR start "ruleModel"
    // InternalLIRAs.g:71:1: ruleModel returns [EObject current=null] : ( (this_NL_0= RULE_NL )* ( (lv_pattern_1_0= rulePattern ) ) ( (lv_sequence_2_0= ruleSequence ) )* ( (lv_conclusion_3_0= ruleConclusion ) )? ) ;
    public final EObject ruleModel() throws RecognitionException {
        EObject current = null;

        Token this_NL_0=null;
        EObject lv_pattern_1_0 = null;

        EObject lv_sequence_2_0 = null;

        EObject lv_conclusion_3_0 = null;



        	enterRule();

        try {
            // InternalLIRAs.g:77:2: ( ( (this_NL_0= RULE_NL )* ( (lv_pattern_1_0= rulePattern ) ) ( (lv_sequence_2_0= ruleSequence ) )* ( (lv_conclusion_3_0= ruleConclusion ) )? ) )
            // InternalLIRAs.g:78:2: ( (this_NL_0= RULE_NL )* ( (lv_pattern_1_0= rulePattern ) ) ( (lv_sequence_2_0= ruleSequence ) )* ( (lv_conclusion_3_0= ruleConclusion ) )? )
            {
            // InternalLIRAs.g:78:2: ( (this_NL_0= RULE_NL )* ( (lv_pattern_1_0= rulePattern ) ) ( (lv_sequence_2_0= ruleSequence ) )* ( (lv_conclusion_3_0= ruleConclusion ) )? )
            // InternalLIRAs.g:79:3: (this_NL_0= RULE_NL )* ( (lv_pattern_1_0= rulePattern ) ) ( (lv_sequence_2_0= ruleSequence ) )* ( (lv_conclusion_3_0= ruleConclusion ) )?
            {
            // InternalLIRAs.g:79:3: (this_NL_0= RULE_NL )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==RULE_NL) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalLIRAs.g:80:4: this_NL_0= RULE_NL
            	    {
            	    this_NL_0=(Token)match(input,RULE_NL,FOLLOW_3); 

            	    				newLeafNode(this_NL_0, grammarAccess.getModelAccess().getNLTerminalRuleCall_0());
            	    			

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            // InternalLIRAs.g:85:3: ( (lv_pattern_1_0= rulePattern ) )
            // InternalLIRAs.g:86:4: (lv_pattern_1_0= rulePattern )
            {
            // InternalLIRAs.g:86:4: (lv_pattern_1_0= rulePattern )
            // InternalLIRAs.g:87:5: lv_pattern_1_0= rulePattern
            {

            					newCompositeNode(grammarAccess.getModelAccess().getPatternPatternParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_4);
            lv_pattern_1_0=rulePattern();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getModelRule());
            					}
            					set(
            						current,
            						"pattern",
            						lv_pattern_1_0,
            						"it.polimi.LIRAs.Pattern");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalLIRAs.g:104:3: ( (lv_sequence_2_0= ruleSequence ) )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==RULE_ID) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // InternalLIRAs.g:105:4: (lv_sequence_2_0= ruleSequence )
            	    {
            	    // InternalLIRAs.g:105:4: (lv_sequence_2_0= ruleSequence )
            	    // InternalLIRAs.g:106:5: lv_sequence_2_0= ruleSequence
            	    {

            	    					newCompositeNode(grammarAccess.getModelAccess().getSequenceSequenceParserRuleCall_2_0());
            	    				
            	    pushFollow(FOLLOW_4);
            	    lv_sequence_2_0=ruleSequence();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getModelRule());
            	    					}
            	    					add(
            	    						current,
            	    						"sequence",
            	    						lv_sequence_2_0,
            	    						"it.polimi.LIRAs.Sequence");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            // InternalLIRAs.g:123:3: ( (lv_conclusion_3_0= ruleConclusion ) )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==21) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // InternalLIRAs.g:124:4: (lv_conclusion_3_0= ruleConclusion )
                    {
                    // InternalLIRAs.g:124:4: (lv_conclusion_3_0= ruleConclusion )
                    // InternalLIRAs.g:125:5: lv_conclusion_3_0= ruleConclusion
                    {

                    					newCompositeNode(grammarAccess.getModelAccess().getConclusionConclusionParserRuleCall_3_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_conclusion_3_0=ruleConclusion();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getModelRule());
                    					}
                    					set(
                    						current,
                    						"conclusion",
                    						lv_conclusion_3_0,
                    						"it.polimi.LIRAs.Conclusion");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleModel"


    // $ANTLR start "entryRulePattern"
    // InternalLIRAs.g:146:1: entryRulePattern returns [EObject current=null] : iv_rulePattern= rulePattern EOF ;
    public final EObject entryRulePattern() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePattern = null;


        try {
            // InternalLIRAs.g:146:48: (iv_rulePattern= rulePattern EOF )
            // InternalLIRAs.g:147:2: iv_rulePattern= rulePattern EOF
            {
             newCompositeNode(grammarAccess.getPatternRule()); 
            pushFollow(FOLLOW_1);
            iv_rulePattern=rulePattern();

            state._fsp--;

             current =iv_rulePattern; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePattern"


    // $ANTLR start "rulePattern"
    // InternalLIRAs.g:153:1: rulePattern returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) (this_NL_1= RULE_NL )+ ( (otherlv_2= 'agents:' (this_NL_3= RULE_NL )+ (otherlv_4= 'humans:' ( ( (lv_human_5_0= ruleHuman ) ) (otherlv_6= ',' ( (lv_human_7_0= ruleHuman ) ) )* )? (this_NL_8= RULE_NL )+ )? (otherlv_9= 'robots:' ( ( (lv_robot_10_0= ruleRobot ) ) (otherlv_11= ',' ( (lv_robot_12_0= ruleRobot ) ) )* )? (this_NL_13= RULE_NL )+ )? ) | (otherlv_14= 'locations:' ( ( (lv_location_15_0= ruleLocation ) ) (otherlv_16= ',' ( (lv_location_17_0= ruleLocation ) ) )* )? (this_NL_18= RULE_NL )+ ) | (otherlv_19= 'resources:' ( ( (lv_resources_20_0= ruleResource ) ) (otherlv_21= ',' ( (lv_resources_22_0= ruleResource ) ) )* )? (this_NL_23= RULE_NL )+ ) )* ) ;
    public final EObject rulePattern() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token this_NL_1=null;
        Token otherlv_2=null;
        Token this_NL_3=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        Token this_NL_8=null;
        Token otherlv_9=null;
        Token otherlv_11=null;
        Token this_NL_13=null;
        Token otherlv_14=null;
        Token otherlv_16=null;
        Token this_NL_18=null;
        Token otherlv_19=null;
        Token otherlv_21=null;
        Token this_NL_23=null;
        EObject lv_human_5_0 = null;

        EObject lv_human_7_0 = null;

        EObject lv_robot_10_0 = null;

        EObject lv_robot_12_0 = null;

        EObject lv_location_15_0 = null;

        EObject lv_location_17_0 = null;

        EObject lv_resources_20_0 = null;

        EObject lv_resources_22_0 = null;



        	enterRule();

        try {
            // InternalLIRAs.g:159:2: ( ( ( (lv_name_0_0= RULE_ID ) ) (this_NL_1= RULE_NL )+ ( (otherlv_2= 'agents:' (this_NL_3= RULE_NL )+ (otherlv_4= 'humans:' ( ( (lv_human_5_0= ruleHuman ) ) (otherlv_6= ',' ( (lv_human_7_0= ruleHuman ) ) )* )? (this_NL_8= RULE_NL )+ )? (otherlv_9= 'robots:' ( ( (lv_robot_10_0= ruleRobot ) ) (otherlv_11= ',' ( (lv_robot_12_0= ruleRobot ) ) )* )? (this_NL_13= RULE_NL )+ )? ) | (otherlv_14= 'locations:' ( ( (lv_location_15_0= ruleLocation ) ) (otherlv_16= ',' ( (lv_location_17_0= ruleLocation ) ) )* )? (this_NL_18= RULE_NL )+ ) | (otherlv_19= 'resources:' ( ( (lv_resources_20_0= ruleResource ) ) (otherlv_21= ',' ( (lv_resources_22_0= ruleResource ) ) )* )? (this_NL_23= RULE_NL )+ ) )* ) )
            // InternalLIRAs.g:160:2: ( ( (lv_name_0_0= RULE_ID ) ) (this_NL_1= RULE_NL )+ ( (otherlv_2= 'agents:' (this_NL_3= RULE_NL )+ (otherlv_4= 'humans:' ( ( (lv_human_5_0= ruleHuman ) ) (otherlv_6= ',' ( (lv_human_7_0= ruleHuman ) ) )* )? (this_NL_8= RULE_NL )+ )? (otherlv_9= 'robots:' ( ( (lv_robot_10_0= ruleRobot ) ) (otherlv_11= ',' ( (lv_robot_12_0= ruleRobot ) ) )* )? (this_NL_13= RULE_NL )+ )? ) | (otherlv_14= 'locations:' ( ( (lv_location_15_0= ruleLocation ) ) (otherlv_16= ',' ( (lv_location_17_0= ruleLocation ) ) )* )? (this_NL_18= RULE_NL )+ ) | (otherlv_19= 'resources:' ( ( (lv_resources_20_0= ruleResource ) ) (otherlv_21= ',' ( (lv_resources_22_0= ruleResource ) ) )* )? (this_NL_23= RULE_NL )+ ) )* )
            {
            // InternalLIRAs.g:160:2: ( ( (lv_name_0_0= RULE_ID ) ) (this_NL_1= RULE_NL )+ ( (otherlv_2= 'agents:' (this_NL_3= RULE_NL )+ (otherlv_4= 'humans:' ( ( (lv_human_5_0= ruleHuman ) ) (otherlv_6= ',' ( (lv_human_7_0= ruleHuman ) ) )* )? (this_NL_8= RULE_NL )+ )? (otherlv_9= 'robots:' ( ( (lv_robot_10_0= ruleRobot ) ) (otherlv_11= ',' ( (lv_robot_12_0= ruleRobot ) ) )* )? (this_NL_13= RULE_NL )+ )? ) | (otherlv_14= 'locations:' ( ( (lv_location_15_0= ruleLocation ) ) (otherlv_16= ',' ( (lv_location_17_0= ruleLocation ) ) )* )? (this_NL_18= RULE_NL )+ ) | (otherlv_19= 'resources:' ( ( (lv_resources_20_0= ruleResource ) ) (otherlv_21= ',' ( (lv_resources_22_0= ruleResource ) ) )* )? (this_NL_23= RULE_NL )+ ) )* )
            // InternalLIRAs.g:161:3: ( (lv_name_0_0= RULE_ID ) ) (this_NL_1= RULE_NL )+ ( (otherlv_2= 'agents:' (this_NL_3= RULE_NL )+ (otherlv_4= 'humans:' ( ( (lv_human_5_0= ruleHuman ) ) (otherlv_6= ',' ( (lv_human_7_0= ruleHuman ) ) )* )? (this_NL_8= RULE_NL )+ )? (otherlv_9= 'robots:' ( ( (lv_robot_10_0= ruleRobot ) ) (otherlv_11= ',' ( (lv_robot_12_0= ruleRobot ) ) )* )? (this_NL_13= RULE_NL )+ )? ) | (otherlv_14= 'locations:' ( ( (lv_location_15_0= ruleLocation ) ) (otherlv_16= ',' ( (lv_location_17_0= ruleLocation ) ) )* )? (this_NL_18= RULE_NL )+ ) | (otherlv_19= 'resources:' ( ( (lv_resources_20_0= ruleResource ) ) (otherlv_21= ',' ( (lv_resources_22_0= ruleResource ) ) )* )? (this_NL_23= RULE_NL )+ ) )*
            {
            // InternalLIRAs.g:161:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalLIRAs.g:162:4: (lv_name_0_0= RULE_ID )
            {
            // InternalLIRAs.g:162:4: (lv_name_0_0= RULE_ID )
            // InternalLIRAs.g:163:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_5); 

            					newLeafNode(lv_name_0_0, grammarAccess.getPatternAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getPatternRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            // InternalLIRAs.g:179:3: (this_NL_1= RULE_NL )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==RULE_NL) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // InternalLIRAs.g:180:4: this_NL_1= RULE_NL
            	    {
            	    this_NL_1=(Token)match(input,RULE_NL,FOLLOW_6); 

            	    				newLeafNode(this_NL_1, grammarAccess.getPatternAccess().getNLTerminalRuleCall_1());
            	    			

            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);

            // InternalLIRAs.g:185:3: ( (otherlv_2= 'agents:' (this_NL_3= RULE_NL )+ (otherlv_4= 'humans:' ( ( (lv_human_5_0= ruleHuman ) ) (otherlv_6= ',' ( (lv_human_7_0= ruleHuman ) ) )* )? (this_NL_8= RULE_NL )+ )? (otherlv_9= 'robots:' ( ( (lv_robot_10_0= ruleRobot ) ) (otherlv_11= ',' ( (lv_robot_12_0= ruleRobot ) ) )* )? (this_NL_13= RULE_NL )+ )? ) | (otherlv_14= 'locations:' ( ( (lv_location_15_0= ruleLocation ) ) (otherlv_16= ',' ( (lv_location_17_0= ruleLocation ) ) )* )? (this_NL_18= RULE_NL )+ ) | (otherlv_19= 'resources:' ( ( (lv_resources_20_0= ruleResource ) ) (otherlv_21= ',' ( (lv_resources_22_0= ruleResource ) ) )* )? (this_NL_23= RULE_NL )+ ) )*
            loop20:
            do {
                int alt20=4;
                switch ( input.LA(1) ) {
                case 12:
                    {
                    alt20=1;
                    }
                    break;
                case 16:
                    {
                    alt20=2;
                    }
                    break;
                case 17:
                    {
                    alt20=3;
                    }
                    break;

                }

                switch (alt20) {
            	case 1 :
            	    // InternalLIRAs.g:186:4: (otherlv_2= 'agents:' (this_NL_3= RULE_NL )+ (otherlv_4= 'humans:' ( ( (lv_human_5_0= ruleHuman ) ) (otherlv_6= ',' ( (lv_human_7_0= ruleHuman ) ) )* )? (this_NL_8= RULE_NL )+ )? (otherlv_9= 'robots:' ( ( (lv_robot_10_0= ruleRobot ) ) (otherlv_11= ',' ( (lv_robot_12_0= ruleRobot ) ) )* )? (this_NL_13= RULE_NL )+ )? )
            	    {
            	    // InternalLIRAs.g:186:4: (otherlv_2= 'agents:' (this_NL_3= RULE_NL )+ (otherlv_4= 'humans:' ( ( (lv_human_5_0= ruleHuman ) ) (otherlv_6= ',' ( (lv_human_7_0= ruleHuman ) ) )* )? (this_NL_8= RULE_NL )+ )? (otherlv_9= 'robots:' ( ( (lv_robot_10_0= ruleRobot ) ) (otherlv_11= ',' ( (lv_robot_12_0= ruleRobot ) ) )* )? (this_NL_13= RULE_NL )+ )? )
            	    // InternalLIRAs.g:187:5: otherlv_2= 'agents:' (this_NL_3= RULE_NL )+ (otherlv_4= 'humans:' ( ( (lv_human_5_0= ruleHuman ) ) (otherlv_6= ',' ( (lv_human_7_0= ruleHuman ) ) )* )? (this_NL_8= RULE_NL )+ )? (otherlv_9= 'robots:' ( ( (lv_robot_10_0= ruleRobot ) ) (otherlv_11= ',' ( (lv_robot_12_0= ruleRobot ) ) )* )? (this_NL_13= RULE_NL )+ )?
            	    {
            	    otherlv_2=(Token)match(input,12,FOLLOW_5); 

            	    					newLeafNode(otherlv_2, grammarAccess.getPatternAccess().getAgentsKeyword_2_0_0());
            	    				
            	    // InternalLIRAs.g:191:5: (this_NL_3= RULE_NL )+
            	    int cnt5=0;
            	    loop5:
            	    do {
            	        int alt5=2;
            	        int LA5_0 = input.LA(1);

            	        if ( (LA5_0==RULE_NL) ) {
            	            alt5=1;
            	        }


            	        switch (alt5) {
            	    	case 1 :
            	    	    // InternalLIRAs.g:192:6: this_NL_3= RULE_NL
            	    	    {
            	    	    this_NL_3=(Token)match(input,RULE_NL,FOLLOW_7); 

            	    	    						newLeafNode(this_NL_3, grammarAccess.getPatternAccess().getNLTerminalRuleCall_2_0_1());
            	    	    					

            	    	    }
            	    	    break;

            	    	default :
            	    	    if ( cnt5 >= 1 ) break loop5;
            	                EarlyExitException eee =
            	                    new EarlyExitException(5, input);
            	                throw eee;
            	        }
            	        cnt5++;
            	    } while (true);

            	    // InternalLIRAs.g:197:5: (otherlv_4= 'humans:' ( ( (lv_human_5_0= ruleHuman ) ) (otherlv_6= ',' ( (lv_human_7_0= ruleHuman ) ) )* )? (this_NL_8= RULE_NL )+ )?
            	    int alt9=2;
            	    int LA9_0 = input.LA(1);

            	    if ( (LA9_0==13) ) {
            	        alt9=1;
            	    }
            	    switch (alt9) {
            	        case 1 :
            	            // InternalLIRAs.g:198:6: otherlv_4= 'humans:' ( ( (lv_human_5_0= ruleHuman ) ) (otherlv_6= ',' ( (lv_human_7_0= ruleHuman ) ) )* )? (this_NL_8= RULE_NL )+
            	            {
            	            otherlv_4=(Token)match(input,13,FOLLOW_3); 

            	            						newLeafNode(otherlv_4, grammarAccess.getPatternAccess().getHumansKeyword_2_0_2_0());
            	            					
            	            // InternalLIRAs.g:202:6: ( ( (lv_human_5_0= ruleHuman ) ) (otherlv_6= ',' ( (lv_human_7_0= ruleHuman ) ) )* )?
            	            int alt7=2;
            	            int LA7_0 = input.LA(1);

            	            if ( (LA7_0==RULE_ID) ) {
            	                alt7=1;
            	            }
            	            switch (alt7) {
            	                case 1 :
            	                    // InternalLIRAs.g:203:7: ( (lv_human_5_0= ruleHuman ) ) (otherlv_6= ',' ( (lv_human_7_0= ruleHuman ) ) )*
            	                    {
            	                    // InternalLIRAs.g:203:7: ( (lv_human_5_0= ruleHuman ) )
            	                    // InternalLIRAs.g:204:8: (lv_human_5_0= ruleHuman )
            	                    {
            	                    // InternalLIRAs.g:204:8: (lv_human_5_0= ruleHuman )
            	                    // InternalLIRAs.g:205:9: lv_human_5_0= ruleHuman
            	                    {

            	                    									newCompositeNode(grammarAccess.getPatternAccess().getHumanHumanParserRuleCall_2_0_2_1_0_0());
            	                    								
            	                    pushFollow(FOLLOW_8);
            	                    lv_human_5_0=ruleHuman();

            	                    state._fsp--;


            	                    									if (current==null) {
            	                    										current = createModelElementForParent(grammarAccess.getPatternRule());
            	                    									}
            	                    									add(
            	                    										current,
            	                    										"human",
            	                    										lv_human_5_0,
            	                    										"it.polimi.LIRAs.Human");
            	                    									afterParserOrEnumRuleCall();
            	                    								

            	                    }


            	                    }

            	                    // InternalLIRAs.g:222:7: (otherlv_6= ',' ( (lv_human_7_0= ruleHuman ) ) )*
            	                    loop6:
            	                    do {
            	                        int alt6=2;
            	                        int LA6_0 = input.LA(1);

            	                        if ( (LA6_0==14) ) {
            	                            alt6=1;
            	                        }


            	                        switch (alt6) {
            	                    	case 1 :
            	                    	    // InternalLIRAs.g:223:8: otherlv_6= ',' ( (lv_human_7_0= ruleHuman ) )
            	                    	    {
            	                    	    otherlv_6=(Token)match(input,14,FOLLOW_9); 

            	                    	    								newLeafNode(otherlv_6, grammarAccess.getPatternAccess().getCommaKeyword_2_0_2_1_1_0());
            	                    	    							
            	                    	    // InternalLIRAs.g:227:8: ( (lv_human_7_0= ruleHuman ) )
            	                    	    // InternalLIRAs.g:228:9: (lv_human_7_0= ruleHuman )
            	                    	    {
            	                    	    // InternalLIRAs.g:228:9: (lv_human_7_0= ruleHuman )
            	                    	    // InternalLIRAs.g:229:10: lv_human_7_0= ruleHuman
            	                    	    {

            	                    	    										newCompositeNode(grammarAccess.getPatternAccess().getHumanHumanParserRuleCall_2_0_2_1_1_1_0());
            	                    	    									
            	                    	    pushFollow(FOLLOW_8);
            	                    	    lv_human_7_0=ruleHuman();

            	                    	    state._fsp--;


            	                    	    										if (current==null) {
            	                    	    											current = createModelElementForParent(grammarAccess.getPatternRule());
            	                    	    										}
            	                    	    										add(
            	                    	    											current,
            	                    	    											"human",
            	                    	    											lv_human_7_0,
            	                    	    											"it.polimi.LIRAs.Human");
            	                    	    										afterParserOrEnumRuleCall();
            	                    	    									

            	                    	    }


            	                    	    }


            	                    	    }
            	                    	    break;

            	                    	default :
            	                    	    break loop6;
            	                        }
            	                    } while (true);


            	                    }
            	                    break;

            	            }

            	            // InternalLIRAs.g:248:6: (this_NL_8= RULE_NL )+
            	            int cnt8=0;
            	            loop8:
            	            do {
            	                int alt8=2;
            	                int LA8_0 = input.LA(1);

            	                if ( (LA8_0==RULE_NL) ) {
            	                    alt8=1;
            	                }


            	                switch (alt8) {
            	            	case 1 :
            	            	    // InternalLIRAs.g:249:7: this_NL_8= RULE_NL
            	            	    {
            	            	    this_NL_8=(Token)match(input,RULE_NL,FOLLOW_10); 

            	            	    							newLeafNode(this_NL_8, grammarAccess.getPatternAccess().getNLTerminalRuleCall_2_0_2_2());
            	            	    						

            	            	    }
            	            	    break;

            	            	default :
            	            	    if ( cnt8 >= 1 ) break loop8;
            	                        EarlyExitException eee =
            	                            new EarlyExitException(8, input);
            	                        throw eee;
            	                }
            	                cnt8++;
            	            } while (true);


            	            }
            	            break;

            	    }

            	    // InternalLIRAs.g:255:5: (otherlv_9= 'robots:' ( ( (lv_robot_10_0= ruleRobot ) ) (otherlv_11= ',' ( (lv_robot_12_0= ruleRobot ) ) )* )? (this_NL_13= RULE_NL )+ )?
            	    int alt13=2;
            	    int LA13_0 = input.LA(1);

            	    if ( (LA13_0==15) ) {
            	        alt13=1;
            	    }
            	    switch (alt13) {
            	        case 1 :
            	            // InternalLIRAs.g:256:6: otherlv_9= 'robots:' ( ( (lv_robot_10_0= ruleRobot ) ) (otherlv_11= ',' ( (lv_robot_12_0= ruleRobot ) ) )* )? (this_NL_13= RULE_NL )+
            	            {
            	            otherlv_9=(Token)match(input,15,FOLLOW_3); 

            	            						newLeafNode(otherlv_9, grammarAccess.getPatternAccess().getRobotsKeyword_2_0_3_0());
            	            					
            	            // InternalLIRAs.g:260:6: ( ( (lv_robot_10_0= ruleRobot ) ) (otherlv_11= ',' ( (lv_robot_12_0= ruleRobot ) ) )* )?
            	            int alt11=2;
            	            int LA11_0 = input.LA(1);

            	            if ( (LA11_0==RULE_ID) ) {
            	                alt11=1;
            	            }
            	            switch (alt11) {
            	                case 1 :
            	                    // InternalLIRAs.g:261:7: ( (lv_robot_10_0= ruleRobot ) ) (otherlv_11= ',' ( (lv_robot_12_0= ruleRobot ) ) )*
            	                    {
            	                    // InternalLIRAs.g:261:7: ( (lv_robot_10_0= ruleRobot ) )
            	                    // InternalLIRAs.g:262:8: (lv_robot_10_0= ruleRobot )
            	                    {
            	                    // InternalLIRAs.g:262:8: (lv_robot_10_0= ruleRobot )
            	                    // InternalLIRAs.g:263:9: lv_robot_10_0= ruleRobot
            	                    {

            	                    									newCompositeNode(grammarAccess.getPatternAccess().getRobotRobotParserRuleCall_2_0_3_1_0_0());
            	                    								
            	                    pushFollow(FOLLOW_8);
            	                    lv_robot_10_0=ruleRobot();

            	                    state._fsp--;


            	                    									if (current==null) {
            	                    										current = createModelElementForParent(grammarAccess.getPatternRule());
            	                    									}
            	                    									add(
            	                    										current,
            	                    										"robot",
            	                    										lv_robot_10_0,
            	                    										"it.polimi.LIRAs.Robot");
            	                    									afterParserOrEnumRuleCall();
            	                    								

            	                    }


            	                    }

            	                    // InternalLIRAs.g:280:7: (otherlv_11= ',' ( (lv_robot_12_0= ruleRobot ) ) )*
            	                    loop10:
            	                    do {
            	                        int alt10=2;
            	                        int LA10_0 = input.LA(1);

            	                        if ( (LA10_0==14) ) {
            	                            alt10=1;
            	                        }


            	                        switch (alt10) {
            	                    	case 1 :
            	                    	    // InternalLIRAs.g:281:8: otherlv_11= ',' ( (lv_robot_12_0= ruleRobot ) )
            	                    	    {
            	                    	    otherlv_11=(Token)match(input,14,FOLLOW_9); 

            	                    	    								newLeafNode(otherlv_11, grammarAccess.getPatternAccess().getCommaKeyword_2_0_3_1_1_0());
            	                    	    							
            	                    	    // InternalLIRAs.g:285:8: ( (lv_robot_12_0= ruleRobot ) )
            	                    	    // InternalLIRAs.g:286:9: (lv_robot_12_0= ruleRobot )
            	                    	    {
            	                    	    // InternalLIRAs.g:286:9: (lv_robot_12_0= ruleRobot )
            	                    	    // InternalLIRAs.g:287:10: lv_robot_12_0= ruleRobot
            	                    	    {

            	                    	    										newCompositeNode(grammarAccess.getPatternAccess().getRobotRobotParserRuleCall_2_0_3_1_1_1_0());
            	                    	    									
            	                    	    pushFollow(FOLLOW_8);
            	                    	    lv_robot_12_0=ruleRobot();

            	                    	    state._fsp--;


            	                    	    										if (current==null) {
            	                    	    											current = createModelElementForParent(grammarAccess.getPatternRule());
            	                    	    										}
            	                    	    										add(
            	                    	    											current,
            	                    	    											"robot",
            	                    	    											lv_robot_12_0,
            	                    	    											"it.polimi.LIRAs.Robot");
            	                    	    										afterParserOrEnumRuleCall();
            	                    	    									

            	                    	    }


            	                    	    }


            	                    	    }
            	                    	    break;

            	                    	default :
            	                    	    break loop10;
            	                        }
            	                    } while (true);


            	                    }
            	                    break;

            	            }

            	            // InternalLIRAs.g:306:6: (this_NL_13= RULE_NL )+
            	            int cnt12=0;
            	            loop12:
            	            do {
            	                int alt12=2;
            	                int LA12_0 = input.LA(1);

            	                if ( (LA12_0==RULE_NL) ) {
            	                    alt12=1;
            	                }


            	                switch (alt12) {
            	            	case 1 :
            	            	    // InternalLIRAs.g:307:7: this_NL_13= RULE_NL
            	            	    {
            	            	    this_NL_13=(Token)match(input,RULE_NL,FOLLOW_6); 

            	            	    							newLeafNode(this_NL_13, grammarAccess.getPatternAccess().getNLTerminalRuleCall_2_0_3_2());
            	            	    						

            	            	    }
            	            	    break;

            	            	default :
            	            	    if ( cnt12 >= 1 ) break loop12;
            	                        EarlyExitException eee =
            	                            new EarlyExitException(12, input);
            	                        throw eee;
            	                }
            	                cnt12++;
            	            } while (true);


            	            }
            	            break;

            	    }


            	    }


            	    }
            	    break;
            	case 2 :
            	    // InternalLIRAs.g:315:4: (otherlv_14= 'locations:' ( ( (lv_location_15_0= ruleLocation ) ) (otherlv_16= ',' ( (lv_location_17_0= ruleLocation ) ) )* )? (this_NL_18= RULE_NL )+ )
            	    {
            	    // InternalLIRAs.g:315:4: (otherlv_14= 'locations:' ( ( (lv_location_15_0= ruleLocation ) ) (otherlv_16= ',' ( (lv_location_17_0= ruleLocation ) ) )* )? (this_NL_18= RULE_NL )+ )
            	    // InternalLIRAs.g:316:5: otherlv_14= 'locations:' ( ( (lv_location_15_0= ruleLocation ) ) (otherlv_16= ',' ( (lv_location_17_0= ruleLocation ) ) )* )? (this_NL_18= RULE_NL )+
            	    {
            	    otherlv_14=(Token)match(input,16,FOLLOW_3); 

            	    					newLeafNode(otherlv_14, grammarAccess.getPatternAccess().getLocationsKeyword_2_1_0());
            	    				
            	    // InternalLIRAs.g:320:5: ( ( (lv_location_15_0= ruleLocation ) ) (otherlv_16= ',' ( (lv_location_17_0= ruleLocation ) ) )* )?
            	    int alt15=2;
            	    int LA15_0 = input.LA(1);

            	    if ( (LA15_0==RULE_ID) ) {
            	        alt15=1;
            	    }
            	    switch (alt15) {
            	        case 1 :
            	            // InternalLIRAs.g:321:6: ( (lv_location_15_0= ruleLocation ) ) (otherlv_16= ',' ( (lv_location_17_0= ruleLocation ) ) )*
            	            {
            	            // InternalLIRAs.g:321:6: ( (lv_location_15_0= ruleLocation ) )
            	            // InternalLIRAs.g:322:7: (lv_location_15_0= ruleLocation )
            	            {
            	            // InternalLIRAs.g:322:7: (lv_location_15_0= ruleLocation )
            	            // InternalLIRAs.g:323:8: lv_location_15_0= ruleLocation
            	            {

            	            								newCompositeNode(grammarAccess.getPatternAccess().getLocationLocationParserRuleCall_2_1_1_0_0());
            	            							
            	            pushFollow(FOLLOW_8);
            	            lv_location_15_0=ruleLocation();

            	            state._fsp--;


            	            								if (current==null) {
            	            									current = createModelElementForParent(grammarAccess.getPatternRule());
            	            								}
            	            								add(
            	            									current,
            	            									"location",
            	            									lv_location_15_0,
            	            									"it.polimi.LIRAs.Location");
            	            								afterParserOrEnumRuleCall();
            	            							

            	            }


            	            }

            	            // InternalLIRAs.g:340:6: (otherlv_16= ',' ( (lv_location_17_0= ruleLocation ) ) )*
            	            loop14:
            	            do {
            	                int alt14=2;
            	                int LA14_0 = input.LA(1);

            	                if ( (LA14_0==14) ) {
            	                    alt14=1;
            	                }


            	                switch (alt14) {
            	            	case 1 :
            	            	    // InternalLIRAs.g:341:7: otherlv_16= ',' ( (lv_location_17_0= ruleLocation ) )
            	            	    {
            	            	    otherlv_16=(Token)match(input,14,FOLLOW_9); 

            	            	    							newLeafNode(otherlv_16, grammarAccess.getPatternAccess().getCommaKeyword_2_1_1_1_0());
            	            	    						
            	            	    // InternalLIRAs.g:345:7: ( (lv_location_17_0= ruleLocation ) )
            	            	    // InternalLIRAs.g:346:8: (lv_location_17_0= ruleLocation )
            	            	    {
            	            	    // InternalLIRAs.g:346:8: (lv_location_17_0= ruleLocation )
            	            	    // InternalLIRAs.g:347:9: lv_location_17_0= ruleLocation
            	            	    {

            	            	    									newCompositeNode(grammarAccess.getPatternAccess().getLocationLocationParserRuleCall_2_1_1_1_1_0());
            	            	    								
            	            	    pushFollow(FOLLOW_8);
            	            	    lv_location_17_0=ruleLocation();

            	            	    state._fsp--;


            	            	    									if (current==null) {
            	            	    										current = createModelElementForParent(grammarAccess.getPatternRule());
            	            	    									}
            	            	    									add(
            	            	    										current,
            	            	    										"location",
            	            	    										lv_location_17_0,
            	            	    										"it.polimi.LIRAs.Location");
            	            	    									afterParserOrEnumRuleCall();
            	            	    								

            	            	    }


            	            	    }


            	            	    }
            	            	    break;

            	            	default :
            	            	    break loop14;
            	                }
            	            } while (true);


            	            }
            	            break;

            	    }

            	    // InternalLIRAs.g:366:5: (this_NL_18= RULE_NL )+
            	    int cnt16=0;
            	    loop16:
            	    do {
            	        int alt16=2;
            	        int LA16_0 = input.LA(1);

            	        if ( (LA16_0==RULE_NL) ) {
            	            alt16=1;
            	        }


            	        switch (alt16) {
            	    	case 1 :
            	    	    // InternalLIRAs.g:367:6: this_NL_18= RULE_NL
            	    	    {
            	    	    this_NL_18=(Token)match(input,RULE_NL,FOLLOW_6); 

            	    	    						newLeafNode(this_NL_18, grammarAccess.getPatternAccess().getNLTerminalRuleCall_2_1_2());
            	    	    					

            	    	    }
            	    	    break;

            	    	default :
            	    	    if ( cnt16 >= 1 ) break loop16;
            	                EarlyExitException eee =
            	                    new EarlyExitException(16, input);
            	                throw eee;
            	        }
            	        cnt16++;
            	    } while (true);


            	    }


            	    }
            	    break;
            	case 3 :
            	    // InternalLIRAs.g:374:4: (otherlv_19= 'resources:' ( ( (lv_resources_20_0= ruleResource ) ) (otherlv_21= ',' ( (lv_resources_22_0= ruleResource ) ) )* )? (this_NL_23= RULE_NL )+ )
            	    {
            	    // InternalLIRAs.g:374:4: (otherlv_19= 'resources:' ( ( (lv_resources_20_0= ruleResource ) ) (otherlv_21= ',' ( (lv_resources_22_0= ruleResource ) ) )* )? (this_NL_23= RULE_NL )+ )
            	    // InternalLIRAs.g:375:5: otherlv_19= 'resources:' ( ( (lv_resources_20_0= ruleResource ) ) (otherlv_21= ',' ( (lv_resources_22_0= ruleResource ) ) )* )? (this_NL_23= RULE_NL )+
            	    {
            	    otherlv_19=(Token)match(input,17,FOLLOW_3); 

            	    					newLeafNode(otherlv_19, grammarAccess.getPatternAccess().getResourcesKeyword_2_2_0());
            	    				
            	    // InternalLIRAs.g:379:5: ( ( (lv_resources_20_0= ruleResource ) ) (otherlv_21= ',' ( (lv_resources_22_0= ruleResource ) ) )* )?
            	    int alt18=2;
            	    int LA18_0 = input.LA(1);

            	    if ( (LA18_0==RULE_ID) ) {
            	        alt18=1;
            	    }
            	    switch (alt18) {
            	        case 1 :
            	            // InternalLIRAs.g:380:6: ( (lv_resources_20_0= ruleResource ) ) (otherlv_21= ',' ( (lv_resources_22_0= ruleResource ) ) )*
            	            {
            	            // InternalLIRAs.g:380:6: ( (lv_resources_20_0= ruleResource ) )
            	            // InternalLIRAs.g:381:7: (lv_resources_20_0= ruleResource )
            	            {
            	            // InternalLIRAs.g:381:7: (lv_resources_20_0= ruleResource )
            	            // InternalLIRAs.g:382:8: lv_resources_20_0= ruleResource
            	            {

            	            								newCompositeNode(grammarAccess.getPatternAccess().getResourcesResourceParserRuleCall_2_2_1_0_0());
            	            							
            	            pushFollow(FOLLOW_8);
            	            lv_resources_20_0=ruleResource();

            	            state._fsp--;


            	            								if (current==null) {
            	            									current = createModelElementForParent(grammarAccess.getPatternRule());
            	            								}
            	            								add(
            	            									current,
            	            									"resources",
            	            									lv_resources_20_0,
            	            									"it.polimi.LIRAs.Resource");
            	            								afterParserOrEnumRuleCall();
            	            							

            	            }


            	            }

            	            // InternalLIRAs.g:399:6: (otherlv_21= ',' ( (lv_resources_22_0= ruleResource ) ) )*
            	            loop17:
            	            do {
            	                int alt17=2;
            	                int LA17_0 = input.LA(1);

            	                if ( (LA17_0==14) ) {
            	                    alt17=1;
            	                }


            	                switch (alt17) {
            	            	case 1 :
            	            	    // InternalLIRAs.g:400:7: otherlv_21= ',' ( (lv_resources_22_0= ruleResource ) )
            	            	    {
            	            	    otherlv_21=(Token)match(input,14,FOLLOW_9); 

            	            	    							newLeafNode(otherlv_21, grammarAccess.getPatternAccess().getCommaKeyword_2_2_1_1_0());
            	            	    						
            	            	    // InternalLIRAs.g:404:7: ( (lv_resources_22_0= ruleResource ) )
            	            	    // InternalLIRAs.g:405:8: (lv_resources_22_0= ruleResource )
            	            	    {
            	            	    // InternalLIRAs.g:405:8: (lv_resources_22_0= ruleResource )
            	            	    // InternalLIRAs.g:406:9: lv_resources_22_0= ruleResource
            	            	    {

            	            	    									newCompositeNode(grammarAccess.getPatternAccess().getResourcesResourceParserRuleCall_2_2_1_1_1_0());
            	            	    								
            	            	    pushFollow(FOLLOW_8);
            	            	    lv_resources_22_0=ruleResource();

            	            	    state._fsp--;


            	            	    									if (current==null) {
            	            	    										current = createModelElementForParent(grammarAccess.getPatternRule());
            	            	    									}
            	            	    									add(
            	            	    										current,
            	            	    										"resources",
            	            	    										lv_resources_22_0,
            	            	    										"it.polimi.LIRAs.Resource");
            	            	    									afterParserOrEnumRuleCall();
            	            	    								

            	            	    }


            	            	    }


            	            	    }
            	            	    break;

            	            	default :
            	            	    break loop17;
            	                }
            	            } while (true);


            	            }
            	            break;

            	    }

            	    // InternalLIRAs.g:425:5: (this_NL_23= RULE_NL )+
            	    int cnt19=0;
            	    loop19:
            	    do {
            	        int alt19=2;
            	        int LA19_0 = input.LA(1);

            	        if ( (LA19_0==RULE_NL) ) {
            	            alt19=1;
            	        }


            	        switch (alt19) {
            	    	case 1 :
            	    	    // InternalLIRAs.g:426:6: this_NL_23= RULE_NL
            	    	    {
            	    	    this_NL_23=(Token)match(input,RULE_NL,FOLLOW_6); 

            	    	    						newLeafNode(this_NL_23, grammarAccess.getPatternAccess().getNLTerminalRuleCall_2_2_2());
            	    	    					

            	    	    }
            	    	    break;

            	    	default :
            	    	    if ( cnt19 >= 1 ) break loop19;
            	                EarlyExitException eee =
            	                    new EarlyExitException(19, input);
            	                throw eee;
            	        }
            	        cnt19++;
            	    } while (true);


            	    }


            	    }
            	    break;

            	default :
            	    break loop20;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "rulePattern"


    // $ANTLR start "entryRuleSequence"
    // InternalLIRAs.g:437:1: entryRuleSequence returns [EObject current=null] : iv_ruleSequence= ruleSequence EOF ;
    public final EObject entryRuleSequence() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSequence = null;


        try {
            // InternalLIRAs.g:437:49: (iv_ruleSequence= ruleSequence EOF )
            // InternalLIRAs.g:438:2: iv_ruleSequence= ruleSequence EOF
            {
             newCompositeNode(grammarAccess.getSequenceRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSequence=ruleSequence();

            state._fsp--;

             current =iv_ruleSequence; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSequence"


    // $ANTLR start "ruleSequence"
    // InternalLIRAs.g:444:1: ruleSequence returns [EObject current=null] : ( ( (lv_agent_0_0= ruleAgent ) ) (otherlv_1= 'default' ( (lv_action_2_0= ruleAction ) ) )? otherlv_3= ':' (this_NL_4= RULE_NL )+ ( (lv_subSequence_5_0= ruleSubSequence ) )* ) ;
    public final EObject ruleSequence() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token this_NL_4=null;
        EObject lv_agent_0_0 = null;

        EObject lv_action_2_0 = null;

        EObject lv_subSequence_5_0 = null;



        	enterRule();

        try {
            // InternalLIRAs.g:450:2: ( ( ( (lv_agent_0_0= ruleAgent ) ) (otherlv_1= 'default' ( (lv_action_2_0= ruleAction ) ) )? otherlv_3= ':' (this_NL_4= RULE_NL )+ ( (lv_subSequence_5_0= ruleSubSequence ) )* ) )
            // InternalLIRAs.g:451:2: ( ( (lv_agent_0_0= ruleAgent ) ) (otherlv_1= 'default' ( (lv_action_2_0= ruleAction ) ) )? otherlv_3= ':' (this_NL_4= RULE_NL )+ ( (lv_subSequence_5_0= ruleSubSequence ) )* )
            {
            // InternalLIRAs.g:451:2: ( ( (lv_agent_0_0= ruleAgent ) ) (otherlv_1= 'default' ( (lv_action_2_0= ruleAction ) ) )? otherlv_3= ':' (this_NL_4= RULE_NL )+ ( (lv_subSequence_5_0= ruleSubSequence ) )* )
            // InternalLIRAs.g:452:3: ( (lv_agent_0_0= ruleAgent ) ) (otherlv_1= 'default' ( (lv_action_2_0= ruleAction ) ) )? otherlv_3= ':' (this_NL_4= RULE_NL )+ ( (lv_subSequence_5_0= ruleSubSequence ) )*
            {
            // InternalLIRAs.g:452:3: ( (lv_agent_0_0= ruleAgent ) )
            // InternalLIRAs.g:453:4: (lv_agent_0_0= ruleAgent )
            {
            // InternalLIRAs.g:453:4: (lv_agent_0_0= ruleAgent )
            // InternalLIRAs.g:454:5: lv_agent_0_0= ruleAgent
            {

            					newCompositeNode(grammarAccess.getSequenceAccess().getAgentAgentParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_11);
            lv_agent_0_0=ruleAgent();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getSequenceRule());
            					}
            					set(
            						current,
            						"agent",
            						lv_agent_0_0,
            						"it.polimi.LIRAs.Agent");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalLIRAs.g:471:3: (otherlv_1= 'default' ( (lv_action_2_0= ruleAction ) ) )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==18) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // InternalLIRAs.g:472:4: otherlv_1= 'default' ( (lv_action_2_0= ruleAction ) )
                    {
                    otherlv_1=(Token)match(input,18,FOLLOW_12); 

                    				newLeafNode(otherlv_1, grammarAccess.getSequenceAccess().getDefaultKeyword_1_0());
                    			
                    // InternalLIRAs.g:476:4: ( (lv_action_2_0= ruleAction ) )
                    // InternalLIRAs.g:477:5: (lv_action_2_0= ruleAction )
                    {
                    // InternalLIRAs.g:477:5: (lv_action_2_0= ruleAction )
                    // InternalLIRAs.g:478:6: lv_action_2_0= ruleAction
                    {

                    						newCompositeNode(grammarAccess.getSequenceAccess().getActionActionParserRuleCall_1_1_0());
                    					
                    pushFollow(FOLLOW_13);
                    lv_action_2_0=ruleAction();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSequenceRule());
                    						}
                    						set(
                    							current,
                    							"action",
                    							lv_action_2_0,
                    							"it.polimi.LIRAs.Action");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_3=(Token)match(input,19,FOLLOW_5); 

            			newLeafNode(otherlv_3, grammarAccess.getSequenceAccess().getColonKeyword_2());
            		
            // InternalLIRAs.g:500:3: (this_NL_4= RULE_NL )+
            int cnt22=0;
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( (LA22_0==RULE_NL) ) {
                    alt22=1;
                }


                switch (alt22) {
            	case 1 :
            	    // InternalLIRAs.g:501:4: this_NL_4= RULE_NL
            	    {
            	    this_NL_4=(Token)match(input,RULE_NL,FOLLOW_14); 

            	    				newLeafNode(this_NL_4, grammarAccess.getSequenceAccess().getNLTerminalRuleCall_3());
            	    			

            	    }
            	    break;

            	default :
            	    if ( cnt22 >= 1 ) break loop22;
                        EarlyExitException eee =
                            new EarlyExitException(22, input);
                        throw eee;
                }
                cnt22++;
            } while (true);

            // InternalLIRAs.g:506:3: ( (lv_subSequence_5_0= ruleSubSequence ) )*
            loop23:
            do {
                int alt23=2;
                int LA23_0 = input.LA(1);

                if ( (LA23_0==RULE_INT) ) {
                    alt23=1;
                }


                switch (alt23) {
            	case 1 :
            	    // InternalLIRAs.g:507:4: (lv_subSequence_5_0= ruleSubSequence )
            	    {
            	    // InternalLIRAs.g:507:4: (lv_subSequence_5_0= ruleSubSequence )
            	    // InternalLIRAs.g:508:5: lv_subSequence_5_0= ruleSubSequence
            	    {

            	    					newCompositeNode(grammarAccess.getSequenceAccess().getSubSequenceSubSequenceParserRuleCall_4_0());
            	    				
            	    pushFollow(FOLLOW_15);
            	    lv_subSequence_5_0=ruleSubSequence();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getSequenceRule());
            	    					}
            	    					add(
            	    						current,
            	    						"subSequence",
            	    						lv_subSequence_5_0,
            	    						"it.polimi.LIRAs.SubSequence");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop23;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleSequence"


    // $ANTLR start "entryRuleSubSequence"
    // InternalLIRAs.g:529:1: entryRuleSubSequence returns [EObject current=null] : iv_ruleSubSequence= ruleSubSequence EOF ;
    public final EObject entryRuleSubSequence() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSubSequence = null;


        try {
            // InternalLIRAs.g:529:52: (iv_ruleSubSequence= ruleSubSequence EOF )
            // InternalLIRAs.g:530:2: iv_ruleSubSequence= ruleSubSequence EOF
            {
             newCompositeNode(grammarAccess.getSubSequenceRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSubSequence=ruleSubSequence();

            state._fsp--;

             current =iv_ruleSubSequence; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSubSequence"


    // $ANTLR start "ruleSubSequence"
    // InternalLIRAs.g:536:1: ruleSubSequence returns [EObject current=null] : ( ( (lv_i_0_0= RULE_INT ) ) (otherlv_1= '-' ( (lv_j_2_0= RULE_INT ) ) )? otherlv_3= ':' ( ( ( (lv_loop_4_0= ruleLoop ) ) | ( (lv_actionC_5_0= ruleActionC ) ) )? this_NL_6= RULE_NL )+ ) ;
    public final EObject ruleSubSequence() throws RecognitionException {
        EObject current = null;

        Token lv_i_0_0=null;
        Token otherlv_1=null;
        Token lv_j_2_0=null;
        Token otherlv_3=null;
        Token this_NL_6=null;
        EObject lv_loop_4_0 = null;

        EObject lv_actionC_5_0 = null;



        	enterRule();

        try {
            // InternalLIRAs.g:542:2: ( ( ( (lv_i_0_0= RULE_INT ) ) (otherlv_1= '-' ( (lv_j_2_0= RULE_INT ) ) )? otherlv_3= ':' ( ( ( (lv_loop_4_0= ruleLoop ) ) | ( (lv_actionC_5_0= ruleActionC ) ) )? this_NL_6= RULE_NL )+ ) )
            // InternalLIRAs.g:543:2: ( ( (lv_i_0_0= RULE_INT ) ) (otherlv_1= '-' ( (lv_j_2_0= RULE_INT ) ) )? otherlv_3= ':' ( ( ( (lv_loop_4_0= ruleLoop ) ) | ( (lv_actionC_5_0= ruleActionC ) ) )? this_NL_6= RULE_NL )+ )
            {
            // InternalLIRAs.g:543:2: ( ( (lv_i_0_0= RULE_INT ) ) (otherlv_1= '-' ( (lv_j_2_0= RULE_INT ) ) )? otherlv_3= ':' ( ( ( (lv_loop_4_0= ruleLoop ) ) | ( (lv_actionC_5_0= ruleActionC ) ) )? this_NL_6= RULE_NL )+ )
            // InternalLIRAs.g:544:3: ( (lv_i_0_0= RULE_INT ) ) (otherlv_1= '-' ( (lv_j_2_0= RULE_INT ) ) )? otherlv_3= ':' ( ( ( (lv_loop_4_0= ruleLoop ) ) | ( (lv_actionC_5_0= ruleActionC ) ) )? this_NL_6= RULE_NL )+
            {
            // InternalLIRAs.g:544:3: ( (lv_i_0_0= RULE_INT ) )
            // InternalLIRAs.g:545:4: (lv_i_0_0= RULE_INT )
            {
            // InternalLIRAs.g:545:4: (lv_i_0_0= RULE_INT )
            // InternalLIRAs.g:546:5: lv_i_0_0= RULE_INT
            {
            lv_i_0_0=(Token)match(input,RULE_INT,FOLLOW_16); 

            					newLeafNode(lv_i_0_0, grammarAccess.getSubSequenceAccess().getIINTTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getSubSequenceRule());
            					}
            					setWithLastConsumed(
            						current,
            						"i",
            						lv_i_0_0,
            						"org.eclipse.xtext.common.Terminals.INT");
            				

            }


            }

            // InternalLIRAs.g:562:3: (otherlv_1= '-' ( (lv_j_2_0= RULE_INT ) ) )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==20) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // InternalLIRAs.g:563:4: otherlv_1= '-' ( (lv_j_2_0= RULE_INT ) )
                    {
                    otherlv_1=(Token)match(input,20,FOLLOW_17); 

                    				newLeafNode(otherlv_1, grammarAccess.getSubSequenceAccess().getHyphenMinusKeyword_1_0());
                    			
                    // InternalLIRAs.g:567:4: ( (lv_j_2_0= RULE_INT ) )
                    // InternalLIRAs.g:568:5: (lv_j_2_0= RULE_INT )
                    {
                    // InternalLIRAs.g:568:5: (lv_j_2_0= RULE_INT )
                    // InternalLIRAs.g:569:6: lv_j_2_0= RULE_INT
                    {
                    lv_j_2_0=(Token)match(input,RULE_INT,FOLLOW_13); 

                    						newLeafNode(lv_j_2_0, grammarAccess.getSubSequenceAccess().getJINTTerminalRuleCall_1_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getSubSequenceRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"j",
                    							lv_j_2_0,
                    							"org.eclipse.xtext.common.Terminals.INT");
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_3=(Token)match(input,19,FOLLOW_18); 

            			newLeafNode(otherlv_3, grammarAccess.getSubSequenceAccess().getColonKeyword_2());
            		
            // InternalLIRAs.g:590:3: ( ( ( (lv_loop_4_0= ruleLoop ) ) | ( (lv_actionC_5_0= ruleActionC ) ) )? this_NL_6= RULE_NL )+
            int cnt26=0;
            loop26:
            do {
                int alt26=2;
                int LA26_0 = input.LA(1);

                if ( (LA26_0==RULE_ID) ) {
                    int LA26_2 = input.LA(2);

                    if ( ((LA26_2>=RULE_NL && LA26_2<=RULE_ID)||LA26_2==25||LA26_2==27||LA26_2==29||(LA26_2>=32 && LA26_2<=35)||LA26_2==40) ) {
                        alt26=1;
                    }


                }
                else if ( (LA26_0==RULE_NL||LA26_0==23||LA26_0==26||(LA26_0>=30 && LA26_0<=36)||(LA26_0>=39 && LA26_0<=40)) ) {
                    alt26=1;
                }


                switch (alt26) {
            	case 1 :
            	    // InternalLIRAs.g:591:4: ( ( (lv_loop_4_0= ruleLoop ) ) | ( (lv_actionC_5_0= ruleActionC ) ) )? this_NL_6= RULE_NL
            	    {
            	    // InternalLIRAs.g:591:4: ( ( (lv_loop_4_0= ruleLoop ) ) | ( (lv_actionC_5_0= ruleActionC ) ) )?
            	    int alt25=3;
            	    int LA25_0 = input.LA(1);

            	    if ( (LA25_0==23||LA25_0==26) ) {
            	        alt25=1;
            	    }
            	    else if ( (LA25_0==RULE_ID||(LA25_0>=30 && LA25_0<=36)||(LA25_0>=39 && LA25_0<=40)) ) {
            	        alt25=2;
            	    }
            	    switch (alt25) {
            	        case 1 :
            	            // InternalLIRAs.g:592:5: ( (lv_loop_4_0= ruleLoop ) )
            	            {
            	            // InternalLIRAs.g:592:5: ( (lv_loop_4_0= ruleLoop ) )
            	            // InternalLIRAs.g:593:6: (lv_loop_4_0= ruleLoop )
            	            {
            	            // InternalLIRAs.g:593:6: (lv_loop_4_0= ruleLoop )
            	            // InternalLIRAs.g:594:7: lv_loop_4_0= ruleLoop
            	            {

            	            							newCompositeNode(grammarAccess.getSubSequenceAccess().getLoopLoopParserRuleCall_3_0_0_0());
            	            						
            	            pushFollow(FOLLOW_5);
            	            lv_loop_4_0=ruleLoop();

            	            state._fsp--;


            	            							if (current==null) {
            	            								current = createModelElementForParent(grammarAccess.getSubSequenceRule());
            	            							}
            	            							add(
            	            								current,
            	            								"loop",
            	            								lv_loop_4_0,
            	            								"it.polimi.LIRAs.Loop");
            	            							afterParserOrEnumRuleCall();
            	            						

            	            }


            	            }


            	            }
            	            break;
            	        case 2 :
            	            // InternalLIRAs.g:612:5: ( (lv_actionC_5_0= ruleActionC ) )
            	            {
            	            // InternalLIRAs.g:612:5: ( (lv_actionC_5_0= ruleActionC ) )
            	            // InternalLIRAs.g:613:6: (lv_actionC_5_0= ruleActionC )
            	            {
            	            // InternalLIRAs.g:613:6: (lv_actionC_5_0= ruleActionC )
            	            // InternalLIRAs.g:614:7: lv_actionC_5_0= ruleActionC
            	            {

            	            							newCompositeNode(grammarAccess.getSubSequenceAccess().getActionCActionCParserRuleCall_3_0_1_0());
            	            						
            	            pushFollow(FOLLOW_5);
            	            lv_actionC_5_0=ruleActionC();

            	            state._fsp--;


            	            							if (current==null) {
            	            								current = createModelElementForParent(grammarAccess.getSubSequenceRule());
            	            							}
            	            							add(
            	            								current,
            	            								"actionC",
            	            								lv_actionC_5_0,
            	            								"it.polimi.LIRAs.ActionC");
            	            							afterParserOrEnumRuleCall();
            	            						

            	            }


            	            }


            	            }
            	            break;

            	    }

            	    this_NL_6=(Token)match(input,RULE_NL,FOLLOW_19); 

            	    				newLeafNode(this_NL_6, grammarAccess.getSubSequenceAccess().getNLTerminalRuleCall_3_1());
            	    			

            	    }
            	    break;

            	default :
            	    if ( cnt26 >= 1 ) break loop26;
                        EarlyExitException eee =
                            new EarlyExitException(26, input);
                        throw eee;
                }
                cnt26++;
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleSubSequence"


    // $ANTLR start "entryRuleConclusion"
    // InternalLIRAs.g:641:1: entryRuleConclusion returns [EObject current=null] : iv_ruleConclusion= ruleConclusion EOF ;
    public final EObject entryRuleConclusion() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleConclusion = null;


        try {
            // InternalLIRAs.g:641:51: (iv_ruleConclusion= ruleConclusion EOF )
            // InternalLIRAs.g:642:2: iv_ruleConclusion= ruleConclusion EOF
            {
             newCompositeNode(grammarAccess.getConclusionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleConclusion=ruleConclusion();

            state._fsp--;

             current =iv_ruleConclusion; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleConclusion"


    // $ANTLR start "ruleConclusion"
    // InternalLIRAs.g:648:1: ruleConclusion returns [EObject current=null] : (otherlv_0= 'success:' ( (lv_condition_1_0= ruleCondition ) ) (this_NL_2= RULE_NL )* otherlv_3= 'failure:' ( (lv_condition_4_0= ruleCondition ) ) (this_NL_5= RULE_NL )* ) ;
    public final EObject ruleConclusion() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token this_NL_2=null;
        Token otherlv_3=null;
        Token this_NL_5=null;
        EObject lv_condition_1_0 = null;

        EObject lv_condition_4_0 = null;



        	enterRule();

        try {
            // InternalLIRAs.g:654:2: ( (otherlv_0= 'success:' ( (lv_condition_1_0= ruleCondition ) ) (this_NL_2= RULE_NL )* otherlv_3= 'failure:' ( (lv_condition_4_0= ruleCondition ) ) (this_NL_5= RULE_NL )* ) )
            // InternalLIRAs.g:655:2: (otherlv_0= 'success:' ( (lv_condition_1_0= ruleCondition ) ) (this_NL_2= RULE_NL )* otherlv_3= 'failure:' ( (lv_condition_4_0= ruleCondition ) ) (this_NL_5= RULE_NL )* )
            {
            // InternalLIRAs.g:655:2: (otherlv_0= 'success:' ( (lv_condition_1_0= ruleCondition ) ) (this_NL_2= RULE_NL )* otherlv_3= 'failure:' ( (lv_condition_4_0= ruleCondition ) ) (this_NL_5= RULE_NL )* )
            // InternalLIRAs.g:656:3: otherlv_0= 'success:' ( (lv_condition_1_0= ruleCondition ) ) (this_NL_2= RULE_NL )* otherlv_3= 'failure:' ( (lv_condition_4_0= ruleCondition ) ) (this_NL_5= RULE_NL )*
            {
            otherlv_0=(Token)match(input,21,FOLLOW_20); 

            			newLeafNode(otherlv_0, grammarAccess.getConclusionAccess().getSuccessKeyword_0());
            		
            // InternalLIRAs.g:660:3: ( (lv_condition_1_0= ruleCondition ) )
            // InternalLIRAs.g:661:4: (lv_condition_1_0= ruleCondition )
            {
            // InternalLIRAs.g:661:4: (lv_condition_1_0= ruleCondition )
            // InternalLIRAs.g:662:5: lv_condition_1_0= ruleCondition
            {

            					newCompositeNode(grammarAccess.getConclusionAccess().getConditionConditionParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_21);
            lv_condition_1_0=ruleCondition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getConclusionRule());
            					}
            					add(
            						current,
            						"condition",
            						lv_condition_1_0,
            						"it.polimi.LIRAs.Condition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalLIRAs.g:679:3: (this_NL_2= RULE_NL )*
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( (LA27_0==RULE_NL) ) {
                    alt27=1;
                }


                switch (alt27) {
            	case 1 :
            	    // InternalLIRAs.g:680:4: this_NL_2= RULE_NL
            	    {
            	    this_NL_2=(Token)match(input,RULE_NL,FOLLOW_21); 

            	    				newLeafNode(this_NL_2, grammarAccess.getConclusionAccess().getNLTerminalRuleCall_2());
            	    			

            	    }
            	    break;

            	default :
            	    break loop27;
                }
            } while (true);

            otherlv_3=(Token)match(input,22,FOLLOW_20); 

            			newLeafNode(otherlv_3, grammarAccess.getConclusionAccess().getFailureKeyword_3());
            		
            // InternalLIRAs.g:689:3: ( (lv_condition_4_0= ruleCondition ) )
            // InternalLIRAs.g:690:4: (lv_condition_4_0= ruleCondition )
            {
            // InternalLIRAs.g:690:4: (lv_condition_4_0= ruleCondition )
            // InternalLIRAs.g:691:5: lv_condition_4_0= ruleCondition
            {

            					newCompositeNode(grammarAccess.getConclusionAccess().getConditionConditionParserRuleCall_4_0());
            				
            pushFollow(FOLLOW_22);
            lv_condition_4_0=ruleCondition();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getConclusionRule());
            					}
            					add(
            						current,
            						"condition",
            						lv_condition_4_0,
            						"it.polimi.LIRAs.Condition");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalLIRAs.g:708:3: (this_NL_5= RULE_NL )*
            loop28:
            do {
                int alt28=2;
                int LA28_0 = input.LA(1);

                if ( (LA28_0==RULE_NL) ) {
                    alt28=1;
                }


                switch (alt28) {
            	case 1 :
            	    // InternalLIRAs.g:709:4: this_NL_5= RULE_NL
            	    {
            	    this_NL_5=(Token)match(input,RULE_NL,FOLLOW_22); 

            	    				newLeafNode(this_NL_5, grammarAccess.getConclusionAccess().getNLTerminalRuleCall_5());
            	    			

            	    }
            	    break;

            	default :
            	    break loop28;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleConclusion"


    // $ANTLR start "entryRuleLoop"
    // InternalLIRAs.g:718:1: entryRuleLoop returns [EObject current=null] : iv_ruleLoop= ruleLoop EOF ;
    public final EObject entryRuleLoop() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleLoop = null;


        try {
            // InternalLIRAs.g:718:45: (iv_ruleLoop= ruleLoop EOF )
            // InternalLIRAs.g:719:2: iv_ruleLoop= ruleLoop EOF
            {
             newCompositeNode(grammarAccess.getLoopRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleLoop=ruleLoop();

            state._fsp--;

             current =iv_ruleLoop; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleLoop"


    // $ANTLR start "ruleLoop"
    // InternalLIRAs.g:725:1: ruleLoop returns [EObject current=null] : ( (otherlv_0= 'skip' (otherlv_1= 'all' )? otherlv_2= 'if' ( (lv_condition_3_0= ruleCondition ) ) ) | (otherlv_4= 'restart' (otherlv_5= 'all' )? otherlv_6= 'if' ( (lv_condition_7_0= ruleCondition ) ) ) ) ;
    public final EObject ruleLoop() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        EObject lv_condition_3_0 = null;

        EObject lv_condition_7_0 = null;



        	enterRule();

        try {
            // InternalLIRAs.g:731:2: ( ( (otherlv_0= 'skip' (otherlv_1= 'all' )? otherlv_2= 'if' ( (lv_condition_3_0= ruleCondition ) ) ) | (otherlv_4= 'restart' (otherlv_5= 'all' )? otherlv_6= 'if' ( (lv_condition_7_0= ruleCondition ) ) ) ) )
            // InternalLIRAs.g:732:2: ( (otherlv_0= 'skip' (otherlv_1= 'all' )? otherlv_2= 'if' ( (lv_condition_3_0= ruleCondition ) ) ) | (otherlv_4= 'restart' (otherlv_5= 'all' )? otherlv_6= 'if' ( (lv_condition_7_0= ruleCondition ) ) ) )
            {
            // InternalLIRAs.g:732:2: ( (otherlv_0= 'skip' (otherlv_1= 'all' )? otherlv_2= 'if' ( (lv_condition_3_0= ruleCondition ) ) ) | (otherlv_4= 'restart' (otherlv_5= 'all' )? otherlv_6= 'if' ( (lv_condition_7_0= ruleCondition ) ) ) )
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( (LA31_0==23) ) {
                alt31=1;
            }
            else if ( (LA31_0==26) ) {
                alt31=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 31, 0, input);

                throw nvae;
            }
            switch (alt31) {
                case 1 :
                    // InternalLIRAs.g:733:3: (otherlv_0= 'skip' (otherlv_1= 'all' )? otherlv_2= 'if' ( (lv_condition_3_0= ruleCondition ) ) )
                    {
                    // InternalLIRAs.g:733:3: (otherlv_0= 'skip' (otherlv_1= 'all' )? otherlv_2= 'if' ( (lv_condition_3_0= ruleCondition ) ) )
                    // InternalLIRAs.g:734:4: otherlv_0= 'skip' (otherlv_1= 'all' )? otherlv_2= 'if' ( (lv_condition_3_0= ruleCondition ) )
                    {
                    otherlv_0=(Token)match(input,23,FOLLOW_23); 

                    				newLeafNode(otherlv_0, grammarAccess.getLoopAccess().getSkipKeyword_0_0());
                    			
                    // InternalLIRAs.g:738:4: (otherlv_1= 'all' )?
                    int alt29=2;
                    int LA29_0 = input.LA(1);

                    if ( (LA29_0==24) ) {
                        alt29=1;
                    }
                    switch (alt29) {
                        case 1 :
                            // InternalLIRAs.g:739:5: otherlv_1= 'all'
                            {
                            otherlv_1=(Token)match(input,24,FOLLOW_24); 

                            					newLeafNode(otherlv_1, grammarAccess.getLoopAccess().getAllKeyword_0_1());
                            				

                            }
                            break;

                    }

                    otherlv_2=(Token)match(input,25,FOLLOW_20); 

                    				newLeafNode(otherlv_2, grammarAccess.getLoopAccess().getIfKeyword_0_2());
                    			
                    // InternalLIRAs.g:748:4: ( (lv_condition_3_0= ruleCondition ) )
                    // InternalLIRAs.g:749:5: (lv_condition_3_0= ruleCondition )
                    {
                    // InternalLIRAs.g:749:5: (lv_condition_3_0= ruleCondition )
                    // InternalLIRAs.g:750:6: lv_condition_3_0= ruleCondition
                    {

                    						newCompositeNode(grammarAccess.getLoopAccess().getConditionConditionParserRuleCall_0_3_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_condition_3_0=ruleCondition();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getLoopRule());
                    						}
                    						set(
                    							current,
                    							"condition",
                    							lv_condition_3_0,
                    							"it.polimi.LIRAs.Condition");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalLIRAs.g:769:3: (otherlv_4= 'restart' (otherlv_5= 'all' )? otherlv_6= 'if' ( (lv_condition_7_0= ruleCondition ) ) )
                    {
                    // InternalLIRAs.g:769:3: (otherlv_4= 'restart' (otherlv_5= 'all' )? otherlv_6= 'if' ( (lv_condition_7_0= ruleCondition ) ) )
                    // InternalLIRAs.g:770:4: otherlv_4= 'restart' (otherlv_5= 'all' )? otherlv_6= 'if' ( (lv_condition_7_0= ruleCondition ) )
                    {
                    otherlv_4=(Token)match(input,26,FOLLOW_23); 

                    				newLeafNode(otherlv_4, grammarAccess.getLoopAccess().getRestartKeyword_1_0());
                    			
                    // InternalLIRAs.g:774:4: (otherlv_5= 'all' )?
                    int alt30=2;
                    int LA30_0 = input.LA(1);

                    if ( (LA30_0==24) ) {
                        alt30=1;
                    }
                    switch (alt30) {
                        case 1 :
                            // InternalLIRAs.g:775:5: otherlv_5= 'all'
                            {
                            otherlv_5=(Token)match(input,24,FOLLOW_24); 

                            					newLeafNode(otherlv_5, grammarAccess.getLoopAccess().getAllKeyword_1_1());
                            				

                            }
                            break;

                    }

                    otherlv_6=(Token)match(input,25,FOLLOW_20); 

                    				newLeafNode(otherlv_6, grammarAccess.getLoopAccess().getIfKeyword_1_2());
                    			
                    // InternalLIRAs.g:784:4: ( (lv_condition_7_0= ruleCondition ) )
                    // InternalLIRAs.g:785:5: (lv_condition_7_0= ruleCondition )
                    {
                    // InternalLIRAs.g:785:5: (lv_condition_7_0= ruleCondition )
                    // InternalLIRAs.g:786:6: lv_condition_7_0= ruleCondition
                    {

                    						newCompositeNode(grammarAccess.getLoopAccess().getConditionConditionParserRuleCall_1_3_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_condition_7_0=ruleCondition();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getLoopRule());
                    						}
                    						set(
                    							current,
                    							"condition",
                    							lv_condition_7_0,
                    							"it.polimi.LIRAs.Condition");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }


                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleLoop"


    // $ANTLR start "entryRuleConditionalStatement"
    // InternalLIRAs.g:808:1: entryRuleConditionalStatement returns [EObject current=null] : iv_ruleConditionalStatement= ruleConditionalStatement EOF ;
    public final EObject entryRuleConditionalStatement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleConditionalStatement = null;


        try {
            // InternalLIRAs.g:808:61: (iv_ruleConditionalStatement= ruleConditionalStatement EOF )
            // InternalLIRAs.g:809:2: iv_ruleConditionalStatement= ruleConditionalStatement EOF
            {
             newCompositeNode(grammarAccess.getConditionalStatementRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleConditionalStatement=ruleConditionalStatement();

            state._fsp--;

             current =iv_ruleConditionalStatement; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleConditionalStatement"


    // $ANTLR start "ruleConditionalStatement"
    // InternalLIRAs.g:815:1: ruleConditionalStatement returns [EObject current=null] : ( ( () ( (lv_cs1_1_0= 'until' ) ) ( (lv_condition_2_0= ruleCondition ) ) ) | ( ( (lv_cs2_3_0= 'if' ) ) ( (lv_condition_4_0= ruleCondition ) ) otherlv_5= 'else' ( (lv_action_6_0= ruleAction ) ) ) | ( ( (lv_cs3_7_0= 'if' ) ) ( (lv_condition_8_0= ruleCondition ) ) otherlv_9= 'else' ( (lv_action_10_0= ruleAction ) ) otherlv_11= 'until' ( (lv_condition_12_0= ruleCondition ) ) ) | ( ( (lv_cs4_13_0= 'wait' ) ) ( (lv_n_14_0= RULE_INT ) ) ) ) ;
    public final EObject ruleConditionalStatement() throws RecognitionException {
        EObject current = null;

        Token lv_cs1_1_0=null;
        Token lv_cs2_3_0=null;
        Token otherlv_5=null;
        Token lv_cs3_7_0=null;
        Token otherlv_9=null;
        Token otherlv_11=null;
        Token lv_cs4_13_0=null;
        Token lv_n_14_0=null;
        EObject lv_condition_2_0 = null;

        EObject lv_condition_4_0 = null;

        EObject lv_action_6_0 = null;

        EObject lv_condition_8_0 = null;

        EObject lv_action_10_0 = null;

        EObject lv_condition_12_0 = null;



        	enterRule();

        try {
            // InternalLIRAs.g:821:2: ( ( ( () ( (lv_cs1_1_0= 'until' ) ) ( (lv_condition_2_0= ruleCondition ) ) ) | ( ( (lv_cs2_3_0= 'if' ) ) ( (lv_condition_4_0= ruleCondition ) ) otherlv_5= 'else' ( (lv_action_6_0= ruleAction ) ) ) | ( ( (lv_cs3_7_0= 'if' ) ) ( (lv_condition_8_0= ruleCondition ) ) otherlv_9= 'else' ( (lv_action_10_0= ruleAction ) ) otherlv_11= 'until' ( (lv_condition_12_0= ruleCondition ) ) ) | ( ( (lv_cs4_13_0= 'wait' ) ) ( (lv_n_14_0= RULE_INT ) ) ) ) )
            // InternalLIRAs.g:822:2: ( ( () ( (lv_cs1_1_0= 'until' ) ) ( (lv_condition_2_0= ruleCondition ) ) ) | ( ( (lv_cs2_3_0= 'if' ) ) ( (lv_condition_4_0= ruleCondition ) ) otherlv_5= 'else' ( (lv_action_6_0= ruleAction ) ) ) | ( ( (lv_cs3_7_0= 'if' ) ) ( (lv_condition_8_0= ruleCondition ) ) otherlv_9= 'else' ( (lv_action_10_0= ruleAction ) ) otherlv_11= 'until' ( (lv_condition_12_0= ruleCondition ) ) ) | ( ( (lv_cs4_13_0= 'wait' ) ) ( (lv_n_14_0= RULE_INT ) ) ) )
            {
            // InternalLIRAs.g:822:2: ( ( () ( (lv_cs1_1_0= 'until' ) ) ( (lv_condition_2_0= ruleCondition ) ) ) | ( ( (lv_cs2_3_0= 'if' ) ) ( (lv_condition_4_0= ruleCondition ) ) otherlv_5= 'else' ( (lv_action_6_0= ruleAction ) ) ) | ( ( (lv_cs3_7_0= 'if' ) ) ( (lv_condition_8_0= ruleCondition ) ) otherlv_9= 'else' ( (lv_action_10_0= ruleAction ) ) otherlv_11= 'until' ( (lv_condition_12_0= ruleCondition ) ) ) | ( ( (lv_cs4_13_0= 'wait' ) ) ( (lv_n_14_0= RULE_INT ) ) ) )
            int alt32=4;
            alt32 = dfa32.predict(input);
            switch (alt32) {
                case 1 :
                    // InternalLIRAs.g:823:3: ( () ( (lv_cs1_1_0= 'until' ) ) ( (lv_condition_2_0= ruleCondition ) ) )
                    {
                    // InternalLIRAs.g:823:3: ( () ( (lv_cs1_1_0= 'until' ) ) ( (lv_condition_2_0= ruleCondition ) ) )
                    // InternalLIRAs.g:824:4: () ( (lv_cs1_1_0= 'until' ) ) ( (lv_condition_2_0= ruleCondition ) )
                    {
                    // InternalLIRAs.g:824:4: ()
                    // InternalLIRAs.g:825:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getConditionalStatementAccess().getConditionalStatementAction_0_0(),
                    						current);
                    				

                    }

                    // InternalLIRAs.g:831:4: ( (lv_cs1_1_0= 'until' ) )
                    // InternalLIRAs.g:832:5: (lv_cs1_1_0= 'until' )
                    {
                    // InternalLIRAs.g:832:5: (lv_cs1_1_0= 'until' )
                    // InternalLIRAs.g:833:6: lv_cs1_1_0= 'until'
                    {
                    lv_cs1_1_0=(Token)match(input,27,FOLLOW_20); 

                    						newLeafNode(lv_cs1_1_0, grammarAccess.getConditionalStatementAccess().getCs1UntilKeyword_0_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getConditionalStatementRule());
                    						}
                    						setWithLastConsumed(current, "cs1", lv_cs1_1_0, "until");
                    					

                    }


                    }

                    // InternalLIRAs.g:845:4: ( (lv_condition_2_0= ruleCondition ) )
                    // InternalLIRAs.g:846:5: (lv_condition_2_0= ruleCondition )
                    {
                    // InternalLIRAs.g:846:5: (lv_condition_2_0= ruleCondition )
                    // InternalLIRAs.g:847:6: lv_condition_2_0= ruleCondition
                    {

                    						newCompositeNode(grammarAccess.getConditionalStatementAccess().getConditionConditionParserRuleCall_0_2_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_condition_2_0=ruleCondition();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getConditionalStatementRule());
                    						}
                    						add(
                    							current,
                    							"condition",
                    							lv_condition_2_0,
                    							"it.polimi.LIRAs.Condition");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalLIRAs.g:866:3: ( ( (lv_cs2_3_0= 'if' ) ) ( (lv_condition_4_0= ruleCondition ) ) otherlv_5= 'else' ( (lv_action_6_0= ruleAction ) ) )
                    {
                    // InternalLIRAs.g:866:3: ( ( (lv_cs2_3_0= 'if' ) ) ( (lv_condition_4_0= ruleCondition ) ) otherlv_5= 'else' ( (lv_action_6_0= ruleAction ) ) )
                    // InternalLIRAs.g:867:4: ( (lv_cs2_3_0= 'if' ) ) ( (lv_condition_4_0= ruleCondition ) ) otherlv_5= 'else' ( (lv_action_6_0= ruleAction ) )
                    {
                    // InternalLIRAs.g:867:4: ( (lv_cs2_3_0= 'if' ) )
                    // InternalLIRAs.g:868:5: (lv_cs2_3_0= 'if' )
                    {
                    // InternalLIRAs.g:868:5: (lv_cs2_3_0= 'if' )
                    // InternalLIRAs.g:869:6: lv_cs2_3_0= 'if'
                    {
                    lv_cs2_3_0=(Token)match(input,25,FOLLOW_20); 

                    						newLeafNode(lv_cs2_3_0, grammarAccess.getConditionalStatementAccess().getCs2IfKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getConditionalStatementRule());
                    						}
                    						setWithLastConsumed(current, "cs2", lv_cs2_3_0, "if");
                    					

                    }


                    }

                    // InternalLIRAs.g:881:4: ( (lv_condition_4_0= ruleCondition ) )
                    // InternalLIRAs.g:882:5: (lv_condition_4_0= ruleCondition )
                    {
                    // InternalLIRAs.g:882:5: (lv_condition_4_0= ruleCondition )
                    // InternalLIRAs.g:883:6: lv_condition_4_0= ruleCondition
                    {

                    						newCompositeNode(grammarAccess.getConditionalStatementAccess().getConditionConditionParserRuleCall_1_1_0());
                    					
                    pushFollow(FOLLOW_25);
                    lv_condition_4_0=ruleCondition();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getConditionalStatementRule());
                    						}
                    						add(
                    							current,
                    							"condition",
                    							lv_condition_4_0,
                    							"it.polimi.LIRAs.Condition");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    otherlv_5=(Token)match(input,28,FOLLOW_12); 

                    				newLeafNode(otherlv_5, grammarAccess.getConditionalStatementAccess().getElseKeyword_1_2());
                    			
                    // InternalLIRAs.g:904:4: ( (lv_action_6_0= ruleAction ) )
                    // InternalLIRAs.g:905:5: (lv_action_6_0= ruleAction )
                    {
                    // InternalLIRAs.g:905:5: (lv_action_6_0= ruleAction )
                    // InternalLIRAs.g:906:6: lv_action_6_0= ruleAction
                    {

                    						newCompositeNode(grammarAccess.getConditionalStatementAccess().getActionActionParserRuleCall_1_3_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_action_6_0=ruleAction();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getConditionalStatementRule());
                    						}
                    						set(
                    							current,
                    							"action",
                    							lv_action_6_0,
                    							"it.polimi.LIRAs.Action");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalLIRAs.g:925:3: ( ( (lv_cs3_7_0= 'if' ) ) ( (lv_condition_8_0= ruleCondition ) ) otherlv_9= 'else' ( (lv_action_10_0= ruleAction ) ) otherlv_11= 'until' ( (lv_condition_12_0= ruleCondition ) ) )
                    {
                    // InternalLIRAs.g:925:3: ( ( (lv_cs3_7_0= 'if' ) ) ( (lv_condition_8_0= ruleCondition ) ) otherlv_9= 'else' ( (lv_action_10_0= ruleAction ) ) otherlv_11= 'until' ( (lv_condition_12_0= ruleCondition ) ) )
                    // InternalLIRAs.g:926:4: ( (lv_cs3_7_0= 'if' ) ) ( (lv_condition_8_0= ruleCondition ) ) otherlv_9= 'else' ( (lv_action_10_0= ruleAction ) ) otherlv_11= 'until' ( (lv_condition_12_0= ruleCondition ) )
                    {
                    // InternalLIRAs.g:926:4: ( (lv_cs3_7_0= 'if' ) )
                    // InternalLIRAs.g:927:5: (lv_cs3_7_0= 'if' )
                    {
                    // InternalLIRAs.g:927:5: (lv_cs3_7_0= 'if' )
                    // InternalLIRAs.g:928:6: lv_cs3_7_0= 'if'
                    {
                    lv_cs3_7_0=(Token)match(input,25,FOLLOW_20); 

                    						newLeafNode(lv_cs3_7_0, grammarAccess.getConditionalStatementAccess().getCs3IfKeyword_2_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getConditionalStatementRule());
                    						}
                    						setWithLastConsumed(current, "cs3", lv_cs3_7_0, "if");
                    					

                    }


                    }

                    // InternalLIRAs.g:940:4: ( (lv_condition_8_0= ruleCondition ) )
                    // InternalLIRAs.g:941:5: (lv_condition_8_0= ruleCondition )
                    {
                    // InternalLIRAs.g:941:5: (lv_condition_8_0= ruleCondition )
                    // InternalLIRAs.g:942:6: lv_condition_8_0= ruleCondition
                    {

                    						newCompositeNode(grammarAccess.getConditionalStatementAccess().getConditionConditionParserRuleCall_2_1_0());
                    					
                    pushFollow(FOLLOW_25);
                    lv_condition_8_0=ruleCondition();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getConditionalStatementRule());
                    						}
                    						add(
                    							current,
                    							"condition",
                    							lv_condition_8_0,
                    							"it.polimi.LIRAs.Condition");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    otherlv_9=(Token)match(input,28,FOLLOW_12); 

                    				newLeafNode(otherlv_9, grammarAccess.getConditionalStatementAccess().getElseKeyword_2_2());
                    			
                    // InternalLIRAs.g:963:4: ( (lv_action_10_0= ruleAction ) )
                    // InternalLIRAs.g:964:5: (lv_action_10_0= ruleAction )
                    {
                    // InternalLIRAs.g:964:5: (lv_action_10_0= ruleAction )
                    // InternalLIRAs.g:965:6: lv_action_10_0= ruleAction
                    {

                    						newCompositeNode(grammarAccess.getConditionalStatementAccess().getActionActionParserRuleCall_2_3_0());
                    					
                    pushFollow(FOLLOW_26);
                    lv_action_10_0=ruleAction();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getConditionalStatementRule());
                    						}
                    						set(
                    							current,
                    							"action",
                    							lv_action_10_0,
                    							"it.polimi.LIRAs.Action");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    otherlv_11=(Token)match(input,27,FOLLOW_20); 

                    				newLeafNode(otherlv_11, grammarAccess.getConditionalStatementAccess().getUntilKeyword_2_4());
                    			
                    // InternalLIRAs.g:986:4: ( (lv_condition_12_0= ruleCondition ) )
                    // InternalLIRAs.g:987:5: (lv_condition_12_0= ruleCondition )
                    {
                    // InternalLIRAs.g:987:5: (lv_condition_12_0= ruleCondition )
                    // InternalLIRAs.g:988:6: lv_condition_12_0= ruleCondition
                    {

                    						newCompositeNode(grammarAccess.getConditionalStatementAccess().getConditionConditionParserRuleCall_2_5_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_condition_12_0=ruleCondition();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getConditionalStatementRule());
                    						}
                    						add(
                    							current,
                    							"condition",
                    							lv_condition_12_0,
                    							"it.polimi.LIRAs.Condition");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }


                    }
                    break;
                case 4 :
                    // InternalLIRAs.g:1007:3: ( ( (lv_cs4_13_0= 'wait' ) ) ( (lv_n_14_0= RULE_INT ) ) )
                    {
                    // InternalLIRAs.g:1007:3: ( ( (lv_cs4_13_0= 'wait' ) ) ( (lv_n_14_0= RULE_INT ) ) )
                    // InternalLIRAs.g:1008:4: ( (lv_cs4_13_0= 'wait' ) ) ( (lv_n_14_0= RULE_INT ) )
                    {
                    // InternalLIRAs.g:1008:4: ( (lv_cs4_13_0= 'wait' ) )
                    // InternalLIRAs.g:1009:5: (lv_cs4_13_0= 'wait' )
                    {
                    // InternalLIRAs.g:1009:5: (lv_cs4_13_0= 'wait' )
                    // InternalLIRAs.g:1010:6: lv_cs4_13_0= 'wait'
                    {
                    lv_cs4_13_0=(Token)match(input,29,FOLLOW_17); 

                    						newLeafNode(lv_cs4_13_0, grammarAccess.getConditionalStatementAccess().getCs4WaitKeyword_3_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getConditionalStatementRule());
                    						}
                    						setWithLastConsumed(current, "cs4", lv_cs4_13_0, "wait");
                    					

                    }


                    }

                    // InternalLIRAs.g:1022:4: ( (lv_n_14_0= RULE_INT ) )
                    // InternalLIRAs.g:1023:5: (lv_n_14_0= RULE_INT )
                    {
                    // InternalLIRAs.g:1023:5: (lv_n_14_0= RULE_INT )
                    // InternalLIRAs.g:1024:6: lv_n_14_0= RULE_INT
                    {
                    lv_n_14_0=(Token)match(input,RULE_INT,FOLLOW_2); 

                    						newLeafNode(lv_n_14_0, grammarAccess.getConditionalStatementAccess().getNINTTerminalRuleCall_3_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getConditionalStatementRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"n",
                    							lv_n_14_0,
                    							"org.eclipse.xtext.common.Terminals.INT");
                    					

                    }


                    }


                    }


                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleConditionalStatement"


    // $ANTLR start "entryRuleAction"
    // InternalLIRAs.g:1045:1: entryRuleAction returns [EObject current=null] : iv_ruleAction= ruleAction EOF ;
    public final EObject entryRuleAction() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAction = null;


        try {
            // InternalLIRAs.g:1045:47: (iv_ruleAction= ruleAction EOF )
            // InternalLIRAs.g:1046:2: iv_ruleAction= ruleAction EOF
            {
             newCompositeNode(grammarAccess.getActionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAction=ruleAction();

            state._fsp--;

             current =iv_ruleAction; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAction"


    // $ANTLR start "ruleAction"
    // InternalLIRAs.g:1052:1: ruleAction returns [EObject current=null] : ( ( () ( (lv_actionType_1_0= 'stop' ) ) ) | ( ( (lv_actionType_2_0= 'moveTo' ) ) ( (lv_target_3_0= ruleTarget ) ) ) | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deliver' ) ) | ( (lv_actionType_8_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_9_0= ruleResource ) ) ) ) ) )+ {...}?) ) ) | ( ( (lv_actionType_10_0= 'follow' ) ) ( (lv_agent_11_0= ruleAgent ) ) (otherlv_12= '(' ( (lv_target_13_0= ruleTarget ) ) otherlv_14= ')' )? ) | ( ( (lv_actionType_15_0= 'receiveFrom' ) ) ( (lv_agent_16_0= ruleAgent ) ) ) ) ;
    public final EObject ruleAction() throws RecognitionException {
        EObject current = null;

        Token lv_actionType_1_0=null;
        Token lv_actionType_2_0=null;
        Token lv_actionType_5_0=null;
        Token lv_actionType_6_0=null;
        Token lv_actionType_7_0=null;
        Token lv_actionType_8_0=null;
        Token lv_actionType_10_0=null;
        Token otherlv_12=null;
        Token otherlv_14=null;
        Token lv_actionType_15_0=null;
        EObject lv_target_3_0 = null;

        EObject lv_resource_9_0 = null;

        EObject lv_agent_11_0 = null;

        EObject lv_target_13_0 = null;

        EObject lv_agent_16_0 = null;



        	enterRule();

        try {
            // InternalLIRAs.g:1058:2: ( ( ( () ( (lv_actionType_1_0= 'stop' ) ) ) | ( ( (lv_actionType_2_0= 'moveTo' ) ) ( (lv_target_3_0= ruleTarget ) ) ) | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deliver' ) ) | ( (lv_actionType_8_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_9_0= ruleResource ) ) ) ) ) )+ {...}?) ) ) | ( ( (lv_actionType_10_0= 'follow' ) ) ( (lv_agent_11_0= ruleAgent ) ) (otherlv_12= '(' ( (lv_target_13_0= ruleTarget ) ) otherlv_14= ')' )? ) | ( ( (lv_actionType_15_0= 'receiveFrom' ) ) ( (lv_agent_16_0= ruleAgent ) ) ) ) )
            // InternalLIRAs.g:1059:2: ( ( () ( (lv_actionType_1_0= 'stop' ) ) ) | ( ( (lv_actionType_2_0= 'moveTo' ) ) ( (lv_target_3_0= ruleTarget ) ) ) | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deliver' ) ) | ( (lv_actionType_8_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_9_0= ruleResource ) ) ) ) ) )+ {...}?) ) ) | ( ( (lv_actionType_10_0= 'follow' ) ) ( (lv_agent_11_0= ruleAgent ) ) (otherlv_12= '(' ( (lv_target_13_0= ruleTarget ) ) otherlv_14= ')' )? ) | ( ( (lv_actionType_15_0= 'receiveFrom' ) ) ( (lv_agent_16_0= ruleAgent ) ) ) )
            {
            // InternalLIRAs.g:1059:2: ( ( () ( (lv_actionType_1_0= 'stop' ) ) ) | ( ( (lv_actionType_2_0= 'moveTo' ) ) ( (lv_target_3_0= ruleTarget ) ) ) | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deliver' ) ) | ( (lv_actionType_8_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_9_0= ruleResource ) ) ) ) ) )+ {...}?) ) ) | ( ( (lv_actionType_10_0= 'follow' ) ) ( (lv_agent_11_0= ruleAgent ) ) (otherlv_12= '(' ( (lv_target_13_0= ruleTarget ) ) otherlv_14= ')' )? ) | ( ( (lv_actionType_15_0= 'receiveFrom' ) ) ( (lv_agent_16_0= ruleAgent ) ) ) )
            int alt36=5;
            switch ( input.LA(1) ) {
            case 30:
                {
                alt36=1;
                }
                break;
            case 31:
                {
                alt36=2;
                }
                break;
            case RULE_ID:
            case 32:
            case 33:
            case 34:
            case 35:
                {
                alt36=3;
                }
                break;
            case 36:
                {
                alt36=4;
                }
                break;
            case 39:
                {
                alt36=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 36, 0, input);

                throw nvae;
            }

            switch (alt36) {
                case 1 :
                    // InternalLIRAs.g:1060:3: ( () ( (lv_actionType_1_0= 'stop' ) ) )
                    {
                    // InternalLIRAs.g:1060:3: ( () ( (lv_actionType_1_0= 'stop' ) ) )
                    // InternalLIRAs.g:1061:4: () ( (lv_actionType_1_0= 'stop' ) )
                    {
                    // InternalLIRAs.g:1061:4: ()
                    // InternalLIRAs.g:1062:5: 
                    {

                    					current = forceCreateModelElement(
                    						grammarAccess.getActionAccess().getActionAction_0_0(),
                    						current);
                    				

                    }

                    // InternalLIRAs.g:1068:4: ( (lv_actionType_1_0= 'stop' ) )
                    // InternalLIRAs.g:1069:5: (lv_actionType_1_0= 'stop' )
                    {
                    // InternalLIRAs.g:1069:5: (lv_actionType_1_0= 'stop' )
                    // InternalLIRAs.g:1070:6: lv_actionType_1_0= 'stop'
                    {
                    lv_actionType_1_0=(Token)match(input,30,FOLLOW_2); 

                    						newLeafNode(lv_actionType_1_0, grammarAccess.getActionAccess().getActionTypeStopKeyword_0_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getActionRule());
                    						}
                    						setWithLastConsumed(current, "actionType", lv_actionType_1_0, "stop");
                    					

                    }


                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalLIRAs.g:1084:3: ( ( (lv_actionType_2_0= 'moveTo' ) ) ( (lv_target_3_0= ruleTarget ) ) )
                    {
                    // InternalLIRAs.g:1084:3: ( ( (lv_actionType_2_0= 'moveTo' ) ) ( (lv_target_3_0= ruleTarget ) ) )
                    // InternalLIRAs.g:1085:4: ( (lv_actionType_2_0= 'moveTo' ) ) ( (lv_target_3_0= ruleTarget ) )
                    {
                    // InternalLIRAs.g:1085:4: ( (lv_actionType_2_0= 'moveTo' ) )
                    // InternalLIRAs.g:1086:5: (lv_actionType_2_0= 'moveTo' )
                    {
                    // InternalLIRAs.g:1086:5: (lv_actionType_2_0= 'moveTo' )
                    // InternalLIRAs.g:1087:6: lv_actionType_2_0= 'moveTo'
                    {
                    lv_actionType_2_0=(Token)match(input,31,FOLLOW_9); 

                    						newLeafNode(lv_actionType_2_0, grammarAccess.getActionAccess().getActionTypeMoveToKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getActionRule());
                    						}
                    						setWithLastConsumed(current, "actionType", lv_actionType_2_0, "moveTo");
                    					

                    }


                    }

                    // InternalLIRAs.g:1099:4: ( (lv_target_3_0= ruleTarget ) )
                    // InternalLIRAs.g:1100:5: (lv_target_3_0= ruleTarget )
                    {
                    // InternalLIRAs.g:1100:5: (lv_target_3_0= ruleTarget )
                    // InternalLIRAs.g:1101:6: lv_target_3_0= ruleTarget
                    {

                    						newCompositeNode(grammarAccess.getActionAccess().getTargetTargetParserRuleCall_1_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_target_3_0=ruleTarget();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getActionRule());
                    						}
                    						set(
                    							current,
                    							"target",
                    							lv_target_3_0,
                    							"it.polimi.LIRAs.Target");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalLIRAs.g:1120:3: ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deliver' ) ) | ( (lv_actionType_8_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_9_0= ruleResource ) ) ) ) ) )+ {...}?) ) )
                    {
                    // InternalLIRAs.g:1120:3: ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deliver' ) ) | ( (lv_actionType_8_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_9_0= ruleResource ) ) ) ) ) )+ {...}?) ) )
                    // InternalLIRAs.g:1121:4: ( ( ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deliver' ) ) | ( (lv_actionType_8_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_9_0= ruleResource ) ) ) ) ) )+ {...}?) )
                    {
                    // InternalLIRAs.g:1121:4: ( ( ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deliver' ) ) | ( (lv_actionType_8_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_9_0= ruleResource ) ) ) ) ) )+ {...}?) )
                    // InternalLIRAs.g:1122:5: ( ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deliver' ) ) | ( (lv_actionType_8_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_9_0= ruleResource ) ) ) ) ) )+ {...}?)
                    {
                     
                    				  getUnorderedGroupHelper().enter(grammarAccess.getActionAccess().getUnorderedGroup_2());
                    				
                    // InternalLIRAs.g:1125:5: ( ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deliver' ) ) | ( (lv_actionType_8_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_9_0= ruleResource ) ) ) ) ) )+ {...}?)
                    // InternalLIRAs.g:1126:6: ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deliver' ) ) | ( (lv_actionType_8_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_9_0= ruleResource ) ) ) ) ) )+ {...}?
                    {
                    // InternalLIRAs.g:1126:6: ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deliver' ) ) | ( (lv_actionType_8_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_9_0= ruleResource ) ) ) ) ) )+
                    int cnt34=0;
                    loop34:
                    do {
                        int alt34=3;
                        int LA34_0 = input.LA(1);

                        if ( LA34_0 >= 32 && LA34_0 <= 35 && getUnorderedGroupHelper().canSelect(grammarAccess.getActionAccess().getUnorderedGroup_2(), 0) ) {
                            alt34=1;
                        }
                        else if ( LA34_0 == RULE_ID && getUnorderedGroupHelper().canSelect(grammarAccess.getActionAccess().getUnorderedGroup_2(), 1) ) {
                            alt34=2;
                        }


                        switch (alt34) {
                    	case 1 :
                    	    // InternalLIRAs.g:1127:4: ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deliver' ) ) | ( (lv_actionType_8_0= 'leave' ) ) ) ) ) )
                    	    {
                    	    // InternalLIRAs.g:1127:4: ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deliver' ) ) | ( (lv_actionType_8_0= 'leave' ) ) ) ) ) )
                    	    // InternalLIRAs.g:1128:5: {...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deliver' ) ) | ( (lv_actionType_8_0= 'leave' ) ) ) ) )
                    	    {
                    	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getActionAccess().getUnorderedGroup_2(), 0) ) {
                    	        throw new FailedPredicateException(input, "ruleAction", "getUnorderedGroupHelper().canSelect(grammarAccess.getActionAccess().getUnorderedGroup_2(), 0)");
                    	    }
                    	    // InternalLIRAs.g:1128:103: ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deliver' ) ) | ( (lv_actionType_8_0= 'leave' ) ) ) ) )
                    	    // InternalLIRAs.g:1129:6: ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deliver' ) ) | ( (lv_actionType_8_0= 'leave' ) ) ) )
                    	    {

                    	    						getUnorderedGroupHelper().select(grammarAccess.getActionAccess().getUnorderedGroup_2(), 0);
                    	    					
                    	    // InternalLIRAs.g:1132:9: ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deliver' ) ) | ( (lv_actionType_8_0= 'leave' ) ) ) )
                    	    // InternalLIRAs.g:1132:10: {...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deliver' ) ) | ( (lv_actionType_8_0= 'leave' ) ) )
                    	    {
                    	    if ( !((true)) ) {
                    	        throw new FailedPredicateException(input, "ruleAction", "true");
                    	    }
                    	    // InternalLIRAs.g:1132:19: ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deliver' ) ) | ( (lv_actionType_8_0= 'leave' ) ) )
                    	    int alt33=4;
                    	    switch ( input.LA(1) ) {
                    	    case 32:
                    	        {
                    	        alt33=1;
                    	        }
                    	        break;
                    	    case 33:
                    	        {
                    	        alt33=2;
                    	        }
                    	        break;
                    	    case 34:
                    	        {
                    	        alt33=3;
                    	        }
                    	        break;
                    	    case 35:
                    	        {
                    	        alt33=4;
                    	        }
                    	        break;
                    	    default:
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 33, 0, input);

                    	        throw nvae;
                    	    }

                    	    switch (alt33) {
                    	        case 1 :
                    	            // InternalLIRAs.g:1132:20: ( (lv_actionType_5_0= 'fetch' ) )
                    	            {
                    	            // InternalLIRAs.g:1132:20: ( (lv_actionType_5_0= 'fetch' ) )
                    	            // InternalLIRAs.g:1133:10: (lv_actionType_5_0= 'fetch' )
                    	            {
                    	            // InternalLIRAs.g:1133:10: (lv_actionType_5_0= 'fetch' )
                    	            // InternalLIRAs.g:1134:11: lv_actionType_5_0= 'fetch'
                    	            {
                    	            lv_actionType_5_0=(Token)match(input,32,FOLLOW_27); 

                    	            											newLeafNode(lv_actionType_5_0, grammarAccess.getActionAccess().getActionTypeFetchKeyword_2_0_0_0());
                    	            										

                    	            											if (current==null) {
                    	            												current = createModelElement(grammarAccess.getActionRule());
                    	            											}
                    	            											setWithLastConsumed(current, "actionType", lv_actionType_5_0, "fetch");
                    	            										

                    	            }


                    	            }


                    	            }
                    	            break;
                    	        case 2 :
                    	            // InternalLIRAs.g:1147:9: ( (lv_actionType_6_0= 'pick' ) )
                    	            {
                    	            // InternalLIRAs.g:1147:9: ( (lv_actionType_6_0= 'pick' ) )
                    	            // InternalLIRAs.g:1148:10: (lv_actionType_6_0= 'pick' )
                    	            {
                    	            // InternalLIRAs.g:1148:10: (lv_actionType_6_0= 'pick' )
                    	            // InternalLIRAs.g:1149:11: lv_actionType_6_0= 'pick'
                    	            {
                    	            lv_actionType_6_0=(Token)match(input,33,FOLLOW_27); 

                    	            											newLeafNode(lv_actionType_6_0, grammarAccess.getActionAccess().getActionTypePickKeyword_2_0_1_0());
                    	            										

                    	            											if (current==null) {
                    	            												current = createModelElement(grammarAccess.getActionRule());
                    	            											}
                    	            											setWithLastConsumed(current, "actionType", lv_actionType_6_0, "pick");
                    	            										

                    	            }


                    	            }


                    	            }
                    	            break;
                    	        case 3 :
                    	            // InternalLIRAs.g:1162:9: ( (lv_actionType_7_0= 'deliver' ) )
                    	            {
                    	            // InternalLIRAs.g:1162:9: ( (lv_actionType_7_0= 'deliver' ) )
                    	            // InternalLIRAs.g:1163:10: (lv_actionType_7_0= 'deliver' )
                    	            {
                    	            // InternalLIRAs.g:1163:10: (lv_actionType_7_0= 'deliver' )
                    	            // InternalLIRAs.g:1164:11: lv_actionType_7_0= 'deliver'
                    	            {
                    	            lv_actionType_7_0=(Token)match(input,34,FOLLOW_27); 

                    	            											newLeafNode(lv_actionType_7_0, grammarAccess.getActionAccess().getActionTypeDeliverKeyword_2_0_2_0());
                    	            										

                    	            											if (current==null) {
                    	            												current = createModelElement(grammarAccess.getActionRule());
                    	            											}
                    	            											setWithLastConsumed(current, "actionType", lv_actionType_7_0, "deliver");
                    	            										

                    	            }


                    	            }


                    	            }
                    	            break;
                    	        case 4 :
                    	            // InternalLIRAs.g:1177:9: ( (lv_actionType_8_0= 'leave' ) )
                    	            {
                    	            // InternalLIRAs.g:1177:9: ( (lv_actionType_8_0= 'leave' ) )
                    	            // InternalLIRAs.g:1178:10: (lv_actionType_8_0= 'leave' )
                    	            {
                    	            // InternalLIRAs.g:1178:10: (lv_actionType_8_0= 'leave' )
                    	            // InternalLIRAs.g:1179:11: lv_actionType_8_0= 'leave'
                    	            {
                    	            lv_actionType_8_0=(Token)match(input,35,FOLLOW_27); 

                    	            											newLeafNode(lv_actionType_8_0, grammarAccess.getActionAccess().getActionTypeLeaveKeyword_2_0_3_0());
                    	            										

                    	            											if (current==null) {
                    	            												current = createModelElement(grammarAccess.getActionRule());
                    	            											}
                    	            											setWithLastConsumed(current, "actionType", lv_actionType_8_0, "leave");
                    	            										

                    	            }


                    	            }


                    	            }
                    	            break;

                    	    }


                    	    }

                    	     
                    	    						getUnorderedGroupHelper().returnFromSelection(grammarAccess.getActionAccess().getUnorderedGroup_2());
                    	    					

                    	    }


                    	    }


                    	    }
                    	    break;
                    	case 2 :
                    	    // InternalLIRAs.g:1197:4: ({...}? => ( ({...}? => ( (lv_resource_9_0= ruleResource ) ) ) ) )
                    	    {
                    	    // InternalLIRAs.g:1197:4: ({...}? => ( ({...}? => ( (lv_resource_9_0= ruleResource ) ) ) ) )
                    	    // InternalLIRAs.g:1198:5: {...}? => ( ({...}? => ( (lv_resource_9_0= ruleResource ) ) ) )
                    	    {
                    	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getActionAccess().getUnorderedGroup_2(), 1) ) {
                    	        throw new FailedPredicateException(input, "ruleAction", "getUnorderedGroupHelper().canSelect(grammarAccess.getActionAccess().getUnorderedGroup_2(), 1)");
                    	    }
                    	    // InternalLIRAs.g:1198:103: ( ({...}? => ( (lv_resource_9_0= ruleResource ) ) ) )
                    	    // InternalLIRAs.g:1199:6: ({...}? => ( (lv_resource_9_0= ruleResource ) ) )
                    	    {

                    	    						getUnorderedGroupHelper().select(grammarAccess.getActionAccess().getUnorderedGroup_2(), 1);
                    	    					
                    	    // InternalLIRAs.g:1202:9: ({...}? => ( (lv_resource_9_0= ruleResource ) ) )
                    	    // InternalLIRAs.g:1202:10: {...}? => ( (lv_resource_9_0= ruleResource ) )
                    	    {
                    	    if ( !((true)) ) {
                    	        throw new FailedPredicateException(input, "ruleAction", "true");
                    	    }
                    	    // InternalLIRAs.g:1202:19: ( (lv_resource_9_0= ruleResource ) )
                    	    // InternalLIRAs.g:1202:20: (lv_resource_9_0= ruleResource )
                    	    {
                    	    // InternalLIRAs.g:1202:20: (lv_resource_9_0= ruleResource )
                    	    // InternalLIRAs.g:1203:10: lv_resource_9_0= ruleResource
                    	    {

                    	    										newCompositeNode(grammarAccess.getActionAccess().getResourceResourceParserRuleCall_2_1_0());
                    	    									
                    	    pushFollow(FOLLOW_27);
                    	    lv_resource_9_0=ruleResource();

                    	    state._fsp--;


                    	    										if (current==null) {
                    	    											current = createModelElementForParent(grammarAccess.getActionRule());
                    	    										}
                    	    										set(
                    	    											current,
                    	    											"resource",
                    	    											lv_resource_9_0,
                    	    											"it.polimi.LIRAs.Resource");
                    	    										afterParserOrEnumRuleCall();
                    	    									

                    	    }


                    	    }


                    	    }

                    	     
                    	    						getUnorderedGroupHelper().returnFromSelection(grammarAccess.getActionAccess().getUnorderedGroup_2());
                    	    					

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt34 >= 1 ) break loop34;
                                EarlyExitException eee =
                                    new EarlyExitException(34, input);
                                throw eee;
                        }
                        cnt34++;
                    } while (true);

                    if ( ! getUnorderedGroupHelper().canLeave(grammarAccess.getActionAccess().getUnorderedGroup_2()) ) {
                        throw new FailedPredicateException(input, "ruleAction", "getUnorderedGroupHelper().canLeave(grammarAccess.getActionAccess().getUnorderedGroup_2())");
                    }

                    }


                    }

                     
                    				  getUnorderedGroupHelper().leave(grammarAccess.getActionAccess().getUnorderedGroup_2());
                    				

                    }


                    }
                    break;
                case 4 :
                    // InternalLIRAs.g:1234:3: ( ( (lv_actionType_10_0= 'follow' ) ) ( (lv_agent_11_0= ruleAgent ) ) (otherlv_12= '(' ( (lv_target_13_0= ruleTarget ) ) otherlv_14= ')' )? )
                    {
                    // InternalLIRAs.g:1234:3: ( ( (lv_actionType_10_0= 'follow' ) ) ( (lv_agent_11_0= ruleAgent ) ) (otherlv_12= '(' ( (lv_target_13_0= ruleTarget ) ) otherlv_14= ')' )? )
                    // InternalLIRAs.g:1235:4: ( (lv_actionType_10_0= 'follow' ) ) ( (lv_agent_11_0= ruleAgent ) ) (otherlv_12= '(' ( (lv_target_13_0= ruleTarget ) ) otherlv_14= ')' )?
                    {
                    // InternalLIRAs.g:1235:4: ( (lv_actionType_10_0= 'follow' ) )
                    // InternalLIRAs.g:1236:5: (lv_actionType_10_0= 'follow' )
                    {
                    // InternalLIRAs.g:1236:5: (lv_actionType_10_0= 'follow' )
                    // InternalLIRAs.g:1237:6: lv_actionType_10_0= 'follow'
                    {
                    lv_actionType_10_0=(Token)match(input,36,FOLLOW_9); 

                    						newLeafNode(lv_actionType_10_0, grammarAccess.getActionAccess().getActionTypeFollowKeyword_3_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getActionRule());
                    						}
                    						setWithLastConsumed(current, "actionType", lv_actionType_10_0, "follow");
                    					

                    }


                    }

                    // InternalLIRAs.g:1249:4: ( (lv_agent_11_0= ruleAgent ) )
                    // InternalLIRAs.g:1250:5: (lv_agent_11_0= ruleAgent )
                    {
                    // InternalLIRAs.g:1250:5: (lv_agent_11_0= ruleAgent )
                    // InternalLIRAs.g:1251:6: lv_agent_11_0= ruleAgent
                    {

                    						newCompositeNode(grammarAccess.getActionAccess().getAgentAgentParserRuleCall_3_1_0());
                    					
                    pushFollow(FOLLOW_28);
                    lv_agent_11_0=ruleAgent();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getActionRule());
                    						}
                    						set(
                    							current,
                    							"agent",
                    							lv_agent_11_0,
                    							"it.polimi.LIRAs.Agent");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    // InternalLIRAs.g:1268:4: (otherlv_12= '(' ( (lv_target_13_0= ruleTarget ) ) otherlv_14= ')' )?
                    int alt35=2;
                    int LA35_0 = input.LA(1);

                    if ( (LA35_0==37) ) {
                        alt35=1;
                    }
                    switch (alt35) {
                        case 1 :
                            // InternalLIRAs.g:1269:5: otherlv_12= '(' ( (lv_target_13_0= ruleTarget ) ) otherlv_14= ')'
                            {
                            otherlv_12=(Token)match(input,37,FOLLOW_9); 

                            					newLeafNode(otherlv_12, grammarAccess.getActionAccess().getLeftParenthesisKeyword_3_2_0());
                            				
                            // InternalLIRAs.g:1273:5: ( (lv_target_13_0= ruleTarget ) )
                            // InternalLIRAs.g:1274:6: (lv_target_13_0= ruleTarget )
                            {
                            // InternalLIRAs.g:1274:6: (lv_target_13_0= ruleTarget )
                            // InternalLIRAs.g:1275:7: lv_target_13_0= ruleTarget
                            {

                            							newCompositeNode(grammarAccess.getActionAccess().getTargetTargetParserRuleCall_3_2_1_0());
                            						
                            pushFollow(FOLLOW_29);
                            lv_target_13_0=ruleTarget();

                            state._fsp--;


                            							if (current==null) {
                            								current = createModelElementForParent(grammarAccess.getActionRule());
                            							}
                            							set(
                            								current,
                            								"target",
                            								lv_target_13_0,
                            								"it.polimi.LIRAs.Target");
                            							afterParserOrEnumRuleCall();
                            						

                            }


                            }

                            otherlv_14=(Token)match(input,38,FOLLOW_2); 

                            					newLeafNode(otherlv_14, grammarAccess.getActionAccess().getRightParenthesisKeyword_3_2_2());
                            				

                            }
                            break;

                    }


                    }


                    }
                    break;
                case 5 :
                    // InternalLIRAs.g:1299:3: ( ( (lv_actionType_15_0= 'receiveFrom' ) ) ( (lv_agent_16_0= ruleAgent ) ) )
                    {
                    // InternalLIRAs.g:1299:3: ( ( (lv_actionType_15_0= 'receiveFrom' ) ) ( (lv_agent_16_0= ruleAgent ) ) )
                    // InternalLIRAs.g:1300:4: ( (lv_actionType_15_0= 'receiveFrom' ) ) ( (lv_agent_16_0= ruleAgent ) )
                    {
                    // InternalLIRAs.g:1300:4: ( (lv_actionType_15_0= 'receiveFrom' ) )
                    // InternalLIRAs.g:1301:5: (lv_actionType_15_0= 'receiveFrom' )
                    {
                    // InternalLIRAs.g:1301:5: (lv_actionType_15_0= 'receiveFrom' )
                    // InternalLIRAs.g:1302:6: lv_actionType_15_0= 'receiveFrom'
                    {
                    lv_actionType_15_0=(Token)match(input,39,FOLLOW_9); 

                    						newLeafNode(lv_actionType_15_0, grammarAccess.getActionAccess().getActionTypeReceiveFromKeyword_4_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getActionRule());
                    						}
                    						setWithLastConsumed(current, "actionType", lv_actionType_15_0, "receiveFrom");
                    					

                    }


                    }

                    // InternalLIRAs.g:1314:4: ( (lv_agent_16_0= ruleAgent ) )
                    // InternalLIRAs.g:1315:5: (lv_agent_16_0= ruleAgent )
                    {
                    // InternalLIRAs.g:1315:5: (lv_agent_16_0= ruleAgent )
                    // InternalLIRAs.g:1316:6: lv_agent_16_0= ruleAgent
                    {

                    						newCompositeNode(grammarAccess.getActionAccess().getAgentAgentParserRuleCall_4_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_agent_16_0=ruleAgent();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getActionRule());
                    						}
                    						set(
                    							current,
                    							"agent",
                    							lv_agent_16_0,
                    							"it.polimi.LIRAs.Agent");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }


                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAction"


    // $ANTLR start "entryRuleActionC"
    // InternalLIRAs.g:1338:1: entryRuleActionC returns [EObject current=null] : iv_ruleActionC= ruleActionC EOF ;
    public final EObject entryRuleActionC() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleActionC = null;


        try {
            // InternalLIRAs.g:1338:48: (iv_ruleActionC= ruleActionC EOF )
            // InternalLIRAs.g:1339:2: iv_ruleActionC= ruleActionC EOF
            {
             newCompositeNode(grammarAccess.getActionCRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleActionC=ruleActionC();

            state._fsp--;

             current =iv_ruleActionC; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleActionC"


    // $ANTLR start "ruleActionC"
    // InternalLIRAs.g:1345:1: ruleActionC returns [EObject current=null] : ( ( ( () ( (lv_actionType_1_0= 'stop' ) ) ) | ( ( (lv_actionType_2_0= 'moveTo' ) ) ( (lv_target_3_0= ruleTarget ) ) ) | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deposit' ) ) | ( (lv_actionType_8_0= 'deliver' ) ) | ( (lv_actionType_9_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_10_0= ruleResource ) ) ) ) ) )+ {...}?) ) ) | ( ( (lv_actionType_11_0= 'follow' ) ) ( (lv_agent_12_0= ruleAgent ) ) (otherlv_13= '(' ( (lv_target_14_0= ruleTarget ) ) otherlv_15= ')' )? ) | ( ( (lv_actionType_16_0= 'receiveFrom' ) ) ( (lv_agent_17_0= ruleAgent ) ) ) ) ( (lv_conditionalStatement_18_0= ruleConditionalStatement ) )? ) ;
    public final EObject ruleActionC() throws RecognitionException {
        EObject current = null;

        Token lv_actionType_1_0=null;
        Token lv_actionType_2_0=null;
        Token lv_actionType_5_0=null;
        Token lv_actionType_6_0=null;
        Token lv_actionType_7_0=null;
        Token lv_actionType_8_0=null;
        Token lv_actionType_9_0=null;
        Token lv_actionType_11_0=null;
        Token otherlv_13=null;
        Token otherlv_15=null;
        Token lv_actionType_16_0=null;
        EObject lv_target_3_0 = null;

        EObject lv_resource_10_0 = null;

        EObject lv_agent_12_0 = null;

        EObject lv_target_14_0 = null;

        EObject lv_agent_17_0 = null;

        EObject lv_conditionalStatement_18_0 = null;



        	enterRule();

        try {
            // InternalLIRAs.g:1351:2: ( ( ( ( () ( (lv_actionType_1_0= 'stop' ) ) ) | ( ( (lv_actionType_2_0= 'moveTo' ) ) ( (lv_target_3_0= ruleTarget ) ) ) | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deposit' ) ) | ( (lv_actionType_8_0= 'deliver' ) ) | ( (lv_actionType_9_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_10_0= ruleResource ) ) ) ) ) )+ {...}?) ) ) | ( ( (lv_actionType_11_0= 'follow' ) ) ( (lv_agent_12_0= ruleAgent ) ) (otherlv_13= '(' ( (lv_target_14_0= ruleTarget ) ) otherlv_15= ')' )? ) | ( ( (lv_actionType_16_0= 'receiveFrom' ) ) ( (lv_agent_17_0= ruleAgent ) ) ) ) ( (lv_conditionalStatement_18_0= ruleConditionalStatement ) )? ) )
            // InternalLIRAs.g:1352:2: ( ( ( () ( (lv_actionType_1_0= 'stop' ) ) ) | ( ( (lv_actionType_2_0= 'moveTo' ) ) ( (lv_target_3_0= ruleTarget ) ) ) | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deposit' ) ) | ( (lv_actionType_8_0= 'deliver' ) ) | ( (lv_actionType_9_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_10_0= ruleResource ) ) ) ) ) )+ {...}?) ) ) | ( ( (lv_actionType_11_0= 'follow' ) ) ( (lv_agent_12_0= ruleAgent ) ) (otherlv_13= '(' ( (lv_target_14_0= ruleTarget ) ) otherlv_15= ')' )? ) | ( ( (lv_actionType_16_0= 'receiveFrom' ) ) ( (lv_agent_17_0= ruleAgent ) ) ) ) ( (lv_conditionalStatement_18_0= ruleConditionalStatement ) )? )
            {
            // InternalLIRAs.g:1352:2: ( ( ( () ( (lv_actionType_1_0= 'stop' ) ) ) | ( ( (lv_actionType_2_0= 'moveTo' ) ) ( (lv_target_3_0= ruleTarget ) ) ) | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deposit' ) ) | ( (lv_actionType_8_0= 'deliver' ) ) | ( (lv_actionType_9_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_10_0= ruleResource ) ) ) ) ) )+ {...}?) ) ) | ( ( (lv_actionType_11_0= 'follow' ) ) ( (lv_agent_12_0= ruleAgent ) ) (otherlv_13= '(' ( (lv_target_14_0= ruleTarget ) ) otherlv_15= ')' )? ) | ( ( (lv_actionType_16_0= 'receiveFrom' ) ) ( (lv_agent_17_0= ruleAgent ) ) ) ) ( (lv_conditionalStatement_18_0= ruleConditionalStatement ) )? )
            // InternalLIRAs.g:1353:3: ( ( () ( (lv_actionType_1_0= 'stop' ) ) ) | ( ( (lv_actionType_2_0= 'moveTo' ) ) ( (lv_target_3_0= ruleTarget ) ) ) | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deposit' ) ) | ( (lv_actionType_8_0= 'deliver' ) ) | ( (lv_actionType_9_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_10_0= ruleResource ) ) ) ) ) )+ {...}?) ) ) | ( ( (lv_actionType_11_0= 'follow' ) ) ( (lv_agent_12_0= ruleAgent ) ) (otherlv_13= '(' ( (lv_target_14_0= ruleTarget ) ) otherlv_15= ')' )? ) | ( ( (lv_actionType_16_0= 'receiveFrom' ) ) ( (lv_agent_17_0= ruleAgent ) ) ) ) ( (lv_conditionalStatement_18_0= ruleConditionalStatement ) )?
            {
            // InternalLIRAs.g:1353:3: ( ( () ( (lv_actionType_1_0= 'stop' ) ) ) | ( ( (lv_actionType_2_0= 'moveTo' ) ) ( (lv_target_3_0= ruleTarget ) ) ) | ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deposit' ) ) | ( (lv_actionType_8_0= 'deliver' ) ) | ( (lv_actionType_9_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_10_0= ruleResource ) ) ) ) ) )+ {...}?) ) ) | ( ( (lv_actionType_11_0= 'follow' ) ) ( (lv_agent_12_0= ruleAgent ) ) (otherlv_13= '(' ( (lv_target_14_0= ruleTarget ) ) otherlv_15= ')' )? ) | ( ( (lv_actionType_16_0= 'receiveFrom' ) ) ( (lv_agent_17_0= ruleAgent ) ) ) )
            int alt40=5;
            switch ( input.LA(1) ) {
            case 30:
                {
                alt40=1;
                }
                break;
            case 31:
                {
                alt40=2;
                }
                break;
            case RULE_ID:
            case 32:
            case 33:
            case 34:
            case 35:
            case 40:
                {
                alt40=3;
                }
                break;
            case 36:
                {
                alt40=4;
                }
                break;
            case 39:
                {
                alt40=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 40, 0, input);

                throw nvae;
            }

            switch (alt40) {
                case 1 :
                    // InternalLIRAs.g:1354:4: ( () ( (lv_actionType_1_0= 'stop' ) ) )
                    {
                    // InternalLIRAs.g:1354:4: ( () ( (lv_actionType_1_0= 'stop' ) ) )
                    // InternalLIRAs.g:1355:5: () ( (lv_actionType_1_0= 'stop' ) )
                    {
                    // InternalLIRAs.g:1355:5: ()
                    // InternalLIRAs.g:1356:6: 
                    {

                    						current = forceCreateModelElement(
                    							grammarAccess.getActionCAccess().getActionAction_0_0_0(),
                    							current);
                    					

                    }

                    // InternalLIRAs.g:1362:5: ( (lv_actionType_1_0= 'stop' ) )
                    // InternalLIRAs.g:1363:6: (lv_actionType_1_0= 'stop' )
                    {
                    // InternalLIRAs.g:1363:6: (lv_actionType_1_0= 'stop' )
                    // InternalLIRAs.g:1364:7: lv_actionType_1_0= 'stop'
                    {
                    lv_actionType_1_0=(Token)match(input,30,FOLLOW_30); 

                    							newLeafNode(lv_actionType_1_0, grammarAccess.getActionCAccess().getActionTypeStopKeyword_0_0_1_0());
                    						

                    							if (current==null) {
                    								current = createModelElement(grammarAccess.getActionCRule());
                    							}
                    							setWithLastConsumed(current, "actionType", lv_actionType_1_0, "stop");
                    						

                    }


                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalLIRAs.g:1378:4: ( ( (lv_actionType_2_0= 'moveTo' ) ) ( (lv_target_3_0= ruleTarget ) ) )
                    {
                    // InternalLIRAs.g:1378:4: ( ( (lv_actionType_2_0= 'moveTo' ) ) ( (lv_target_3_0= ruleTarget ) ) )
                    // InternalLIRAs.g:1379:5: ( (lv_actionType_2_0= 'moveTo' ) ) ( (lv_target_3_0= ruleTarget ) )
                    {
                    // InternalLIRAs.g:1379:5: ( (lv_actionType_2_0= 'moveTo' ) )
                    // InternalLIRAs.g:1380:6: (lv_actionType_2_0= 'moveTo' )
                    {
                    // InternalLIRAs.g:1380:6: (lv_actionType_2_0= 'moveTo' )
                    // InternalLIRAs.g:1381:7: lv_actionType_2_0= 'moveTo'
                    {
                    lv_actionType_2_0=(Token)match(input,31,FOLLOW_9); 

                    							newLeafNode(lv_actionType_2_0, grammarAccess.getActionCAccess().getActionTypeMoveToKeyword_0_1_0_0());
                    						

                    							if (current==null) {
                    								current = createModelElement(grammarAccess.getActionCRule());
                    							}
                    							setWithLastConsumed(current, "actionType", lv_actionType_2_0, "moveTo");
                    						

                    }


                    }

                    // InternalLIRAs.g:1393:5: ( (lv_target_3_0= ruleTarget ) )
                    // InternalLIRAs.g:1394:6: (lv_target_3_0= ruleTarget )
                    {
                    // InternalLIRAs.g:1394:6: (lv_target_3_0= ruleTarget )
                    // InternalLIRAs.g:1395:7: lv_target_3_0= ruleTarget
                    {

                    							newCompositeNode(grammarAccess.getActionCAccess().getTargetTargetParserRuleCall_0_1_1_0());
                    						
                    pushFollow(FOLLOW_30);
                    lv_target_3_0=ruleTarget();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getActionCRule());
                    							}
                    							set(
                    								current,
                    								"target",
                    								lv_target_3_0,
                    								"it.polimi.LIRAs.Target");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }


                    }


                    }
                    break;
                case 3 :
                    // InternalLIRAs.g:1414:4: ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deposit' ) ) | ( (lv_actionType_8_0= 'deliver' ) ) | ( (lv_actionType_9_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_10_0= ruleResource ) ) ) ) ) )+ {...}?) ) )
                    {
                    // InternalLIRAs.g:1414:4: ( ( ( ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deposit' ) ) | ( (lv_actionType_8_0= 'deliver' ) ) | ( (lv_actionType_9_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_10_0= ruleResource ) ) ) ) ) )+ {...}?) ) )
                    // InternalLIRAs.g:1415:5: ( ( ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deposit' ) ) | ( (lv_actionType_8_0= 'deliver' ) ) | ( (lv_actionType_9_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_10_0= ruleResource ) ) ) ) ) )+ {...}?) )
                    {
                    // InternalLIRAs.g:1415:5: ( ( ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deposit' ) ) | ( (lv_actionType_8_0= 'deliver' ) ) | ( (lv_actionType_9_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_10_0= ruleResource ) ) ) ) ) )+ {...}?) )
                    // InternalLIRAs.g:1416:6: ( ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deposit' ) ) | ( (lv_actionType_8_0= 'deliver' ) ) | ( (lv_actionType_9_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_10_0= ruleResource ) ) ) ) ) )+ {...}?)
                    {
                     
                    					  getUnorderedGroupHelper().enter(grammarAccess.getActionCAccess().getUnorderedGroup_0_2());
                    					
                    // InternalLIRAs.g:1419:6: ( ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deposit' ) ) | ( (lv_actionType_8_0= 'deliver' ) ) | ( (lv_actionType_9_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_10_0= ruleResource ) ) ) ) ) )+ {...}?)
                    // InternalLIRAs.g:1420:7: ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deposit' ) ) | ( (lv_actionType_8_0= 'deliver' ) ) | ( (lv_actionType_9_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_10_0= ruleResource ) ) ) ) ) )+ {...}?
                    {
                    // InternalLIRAs.g:1420:7: ( ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deposit' ) ) | ( (lv_actionType_8_0= 'deliver' ) ) | ( (lv_actionType_9_0= 'leave' ) ) ) ) ) ) | ({...}? => ( ({...}? => ( (lv_resource_10_0= ruleResource ) ) ) ) ) )+
                    int cnt38=0;
                    loop38:
                    do {
                        int alt38=3;
                        int LA38_0 = input.LA(1);

                        if ( ( LA38_0 >= 32 && LA38_0 <= 35 || LA38_0 == 40 ) && getUnorderedGroupHelper().canSelect(grammarAccess.getActionCAccess().getUnorderedGroup_0_2(), 0) ) {
                            alt38=1;
                        }
                        else if ( LA38_0 == RULE_ID && getUnorderedGroupHelper().canSelect(grammarAccess.getActionCAccess().getUnorderedGroup_0_2(), 1) ) {
                            alt38=2;
                        }


                        switch (alt38) {
                    	case 1 :
                    	    // InternalLIRAs.g:1421:5: ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deposit' ) ) | ( (lv_actionType_8_0= 'deliver' ) ) | ( (lv_actionType_9_0= 'leave' ) ) ) ) ) )
                    	    {
                    	    // InternalLIRAs.g:1421:5: ({...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deposit' ) ) | ( (lv_actionType_8_0= 'deliver' ) ) | ( (lv_actionType_9_0= 'leave' ) ) ) ) ) )
                    	    // InternalLIRAs.g:1422:6: {...}? => ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deposit' ) ) | ( (lv_actionType_8_0= 'deliver' ) ) | ( (lv_actionType_9_0= 'leave' ) ) ) ) )
                    	    {
                    	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getActionCAccess().getUnorderedGroup_0_2(), 0) ) {
                    	        throw new FailedPredicateException(input, "ruleActionC", "getUnorderedGroupHelper().canSelect(grammarAccess.getActionCAccess().getUnorderedGroup_0_2(), 0)");
                    	    }
                    	    // InternalLIRAs.g:1422:107: ( ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deposit' ) ) | ( (lv_actionType_8_0= 'deliver' ) ) | ( (lv_actionType_9_0= 'leave' ) ) ) ) )
                    	    // InternalLIRAs.g:1423:7: ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deposit' ) ) | ( (lv_actionType_8_0= 'deliver' ) ) | ( (lv_actionType_9_0= 'leave' ) ) ) )
                    	    {

                    	    							getUnorderedGroupHelper().select(grammarAccess.getActionCAccess().getUnorderedGroup_0_2(), 0);
                    	    						
                    	    // InternalLIRAs.g:1426:10: ({...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deposit' ) ) | ( (lv_actionType_8_0= 'deliver' ) ) | ( (lv_actionType_9_0= 'leave' ) ) ) )
                    	    // InternalLIRAs.g:1426:11: {...}? => ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deposit' ) ) | ( (lv_actionType_8_0= 'deliver' ) ) | ( (lv_actionType_9_0= 'leave' ) ) )
                    	    {
                    	    if ( !((true)) ) {
                    	        throw new FailedPredicateException(input, "ruleActionC", "true");
                    	    }
                    	    // InternalLIRAs.g:1426:20: ( ( (lv_actionType_5_0= 'fetch' ) ) | ( (lv_actionType_6_0= 'pick' ) ) | ( (lv_actionType_7_0= 'deposit' ) ) | ( (lv_actionType_8_0= 'deliver' ) ) | ( (lv_actionType_9_0= 'leave' ) ) )
                    	    int alt37=5;
                    	    switch ( input.LA(1) ) {
                    	    case 32:
                    	        {
                    	        alt37=1;
                    	        }
                    	        break;
                    	    case 33:
                    	        {
                    	        alt37=2;
                    	        }
                    	        break;
                    	    case 40:
                    	        {
                    	        alt37=3;
                    	        }
                    	        break;
                    	    case 34:
                    	        {
                    	        alt37=4;
                    	        }
                    	        break;
                    	    case 35:
                    	        {
                    	        alt37=5;
                    	        }
                    	        break;
                    	    default:
                    	        NoViableAltException nvae =
                    	            new NoViableAltException("", 37, 0, input);

                    	        throw nvae;
                    	    }

                    	    switch (alt37) {
                    	        case 1 :
                    	            // InternalLIRAs.g:1426:21: ( (lv_actionType_5_0= 'fetch' ) )
                    	            {
                    	            // InternalLIRAs.g:1426:21: ( (lv_actionType_5_0= 'fetch' ) )
                    	            // InternalLIRAs.g:1427:11: (lv_actionType_5_0= 'fetch' )
                    	            {
                    	            // InternalLIRAs.g:1427:11: (lv_actionType_5_0= 'fetch' )
                    	            // InternalLIRAs.g:1428:12: lv_actionType_5_0= 'fetch'
                    	            {
                    	            lv_actionType_5_0=(Token)match(input,32,FOLLOW_31); 

                    	            												newLeafNode(lv_actionType_5_0, grammarAccess.getActionCAccess().getActionTypeFetchKeyword_0_2_0_0_0());
                    	            											

                    	            												if (current==null) {
                    	            													current = createModelElement(grammarAccess.getActionCRule());
                    	            												}
                    	            												setWithLastConsumed(current, "actionType", lv_actionType_5_0, "fetch");
                    	            											

                    	            }


                    	            }


                    	            }
                    	            break;
                    	        case 2 :
                    	            // InternalLIRAs.g:1441:10: ( (lv_actionType_6_0= 'pick' ) )
                    	            {
                    	            // InternalLIRAs.g:1441:10: ( (lv_actionType_6_0= 'pick' ) )
                    	            // InternalLIRAs.g:1442:11: (lv_actionType_6_0= 'pick' )
                    	            {
                    	            // InternalLIRAs.g:1442:11: (lv_actionType_6_0= 'pick' )
                    	            // InternalLIRAs.g:1443:12: lv_actionType_6_0= 'pick'
                    	            {
                    	            lv_actionType_6_0=(Token)match(input,33,FOLLOW_31); 

                    	            												newLeafNode(lv_actionType_6_0, grammarAccess.getActionCAccess().getActionTypePickKeyword_0_2_0_1_0());
                    	            											

                    	            												if (current==null) {
                    	            													current = createModelElement(grammarAccess.getActionCRule());
                    	            												}
                    	            												setWithLastConsumed(current, "actionType", lv_actionType_6_0, "pick");
                    	            											

                    	            }


                    	            }


                    	            }
                    	            break;
                    	        case 3 :
                    	            // InternalLIRAs.g:1456:10: ( (lv_actionType_7_0= 'deposit' ) )
                    	            {
                    	            // InternalLIRAs.g:1456:10: ( (lv_actionType_7_0= 'deposit' ) )
                    	            // InternalLIRAs.g:1457:11: (lv_actionType_7_0= 'deposit' )
                    	            {
                    	            // InternalLIRAs.g:1457:11: (lv_actionType_7_0= 'deposit' )
                    	            // InternalLIRAs.g:1458:12: lv_actionType_7_0= 'deposit'
                    	            {
                    	            lv_actionType_7_0=(Token)match(input,40,FOLLOW_31); 

                    	            												newLeafNode(lv_actionType_7_0, grammarAccess.getActionCAccess().getActionTypeDepositKeyword_0_2_0_2_0());
                    	            											

                    	            												if (current==null) {
                    	            													current = createModelElement(grammarAccess.getActionCRule());
                    	            												}
                    	            												setWithLastConsumed(current, "actionType", lv_actionType_7_0, "deposit");
                    	            											

                    	            }


                    	            }


                    	            }
                    	            break;
                    	        case 4 :
                    	            // InternalLIRAs.g:1471:10: ( (lv_actionType_8_0= 'deliver' ) )
                    	            {
                    	            // InternalLIRAs.g:1471:10: ( (lv_actionType_8_0= 'deliver' ) )
                    	            // InternalLIRAs.g:1472:11: (lv_actionType_8_0= 'deliver' )
                    	            {
                    	            // InternalLIRAs.g:1472:11: (lv_actionType_8_0= 'deliver' )
                    	            // InternalLIRAs.g:1473:12: lv_actionType_8_0= 'deliver'
                    	            {
                    	            lv_actionType_8_0=(Token)match(input,34,FOLLOW_31); 

                    	            												newLeafNode(lv_actionType_8_0, grammarAccess.getActionCAccess().getActionTypeDeliverKeyword_0_2_0_3_0());
                    	            											

                    	            												if (current==null) {
                    	            													current = createModelElement(grammarAccess.getActionCRule());
                    	            												}
                    	            												setWithLastConsumed(current, "actionType", lv_actionType_8_0, "deliver");
                    	            											

                    	            }


                    	            }


                    	            }
                    	            break;
                    	        case 5 :
                    	            // InternalLIRAs.g:1486:10: ( (lv_actionType_9_0= 'leave' ) )
                    	            {
                    	            // InternalLIRAs.g:1486:10: ( (lv_actionType_9_0= 'leave' ) )
                    	            // InternalLIRAs.g:1487:11: (lv_actionType_9_0= 'leave' )
                    	            {
                    	            // InternalLIRAs.g:1487:11: (lv_actionType_9_0= 'leave' )
                    	            // InternalLIRAs.g:1488:12: lv_actionType_9_0= 'leave'
                    	            {
                    	            lv_actionType_9_0=(Token)match(input,35,FOLLOW_31); 

                    	            												newLeafNode(lv_actionType_9_0, grammarAccess.getActionCAccess().getActionTypeLeaveKeyword_0_2_0_4_0());
                    	            											

                    	            												if (current==null) {
                    	            													current = createModelElement(grammarAccess.getActionCRule());
                    	            												}
                    	            												setWithLastConsumed(current, "actionType", lv_actionType_9_0, "leave");
                    	            											

                    	            }


                    	            }


                    	            }
                    	            break;

                    	    }


                    	    }

                    	     
                    	    							getUnorderedGroupHelper().returnFromSelection(grammarAccess.getActionCAccess().getUnorderedGroup_0_2());
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;
                    	case 2 :
                    	    // InternalLIRAs.g:1506:5: ({...}? => ( ({...}? => ( (lv_resource_10_0= ruleResource ) ) ) ) )
                    	    {
                    	    // InternalLIRAs.g:1506:5: ({...}? => ( ({...}? => ( (lv_resource_10_0= ruleResource ) ) ) ) )
                    	    // InternalLIRAs.g:1507:6: {...}? => ( ({...}? => ( (lv_resource_10_0= ruleResource ) ) ) )
                    	    {
                    	    if ( ! getUnorderedGroupHelper().canSelect(grammarAccess.getActionCAccess().getUnorderedGroup_0_2(), 1) ) {
                    	        throw new FailedPredicateException(input, "ruleActionC", "getUnorderedGroupHelper().canSelect(grammarAccess.getActionCAccess().getUnorderedGroup_0_2(), 1)");
                    	    }
                    	    // InternalLIRAs.g:1507:107: ( ({...}? => ( (lv_resource_10_0= ruleResource ) ) ) )
                    	    // InternalLIRAs.g:1508:7: ({...}? => ( (lv_resource_10_0= ruleResource ) ) )
                    	    {

                    	    							getUnorderedGroupHelper().select(grammarAccess.getActionCAccess().getUnorderedGroup_0_2(), 1);
                    	    						
                    	    // InternalLIRAs.g:1511:10: ({...}? => ( (lv_resource_10_0= ruleResource ) ) )
                    	    // InternalLIRAs.g:1511:11: {...}? => ( (lv_resource_10_0= ruleResource ) )
                    	    {
                    	    if ( !((true)) ) {
                    	        throw new FailedPredicateException(input, "ruleActionC", "true");
                    	    }
                    	    // InternalLIRAs.g:1511:20: ( (lv_resource_10_0= ruleResource ) )
                    	    // InternalLIRAs.g:1511:21: (lv_resource_10_0= ruleResource )
                    	    {
                    	    // InternalLIRAs.g:1511:21: (lv_resource_10_0= ruleResource )
                    	    // InternalLIRAs.g:1512:11: lv_resource_10_0= ruleResource
                    	    {

                    	    											newCompositeNode(grammarAccess.getActionCAccess().getResourceResourceParserRuleCall_0_2_1_0());
                    	    										
                    	    pushFollow(FOLLOW_31);
                    	    lv_resource_10_0=ruleResource();

                    	    state._fsp--;


                    	    											if (current==null) {
                    	    												current = createModelElementForParent(grammarAccess.getActionCRule());
                    	    											}
                    	    											set(
                    	    												current,
                    	    												"resource",
                    	    												lv_resource_10_0,
                    	    												"it.polimi.LIRAs.Resource");
                    	    											afterParserOrEnumRuleCall();
                    	    										

                    	    }


                    	    }


                    	    }

                    	     
                    	    							getUnorderedGroupHelper().returnFromSelection(grammarAccess.getActionCAccess().getUnorderedGroup_0_2());
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt38 >= 1 ) break loop38;
                                EarlyExitException eee =
                                    new EarlyExitException(38, input);
                                throw eee;
                        }
                        cnt38++;
                    } while (true);

                    if ( ! getUnorderedGroupHelper().canLeave(grammarAccess.getActionCAccess().getUnorderedGroup_0_2()) ) {
                        throw new FailedPredicateException(input, "ruleActionC", "getUnorderedGroupHelper().canLeave(grammarAccess.getActionCAccess().getUnorderedGroup_0_2())");
                    }

                    }


                    }

                     
                    					  getUnorderedGroupHelper().leave(grammarAccess.getActionCAccess().getUnorderedGroup_0_2());
                    					

                    }


                    }
                    break;
                case 4 :
                    // InternalLIRAs.g:1543:4: ( ( (lv_actionType_11_0= 'follow' ) ) ( (lv_agent_12_0= ruleAgent ) ) (otherlv_13= '(' ( (lv_target_14_0= ruleTarget ) ) otherlv_15= ')' )? )
                    {
                    // InternalLIRAs.g:1543:4: ( ( (lv_actionType_11_0= 'follow' ) ) ( (lv_agent_12_0= ruleAgent ) ) (otherlv_13= '(' ( (lv_target_14_0= ruleTarget ) ) otherlv_15= ')' )? )
                    // InternalLIRAs.g:1544:5: ( (lv_actionType_11_0= 'follow' ) ) ( (lv_agent_12_0= ruleAgent ) ) (otherlv_13= '(' ( (lv_target_14_0= ruleTarget ) ) otherlv_15= ')' )?
                    {
                    // InternalLIRAs.g:1544:5: ( (lv_actionType_11_0= 'follow' ) )
                    // InternalLIRAs.g:1545:6: (lv_actionType_11_0= 'follow' )
                    {
                    // InternalLIRAs.g:1545:6: (lv_actionType_11_0= 'follow' )
                    // InternalLIRAs.g:1546:7: lv_actionType_11_0= 'follow'
                    {
                    lv_actionType_11_0=(Token)match(input,36,FOLLOW_9); 

                    							newLeafNode(lv_actionType_11_0, grammarAccess.getActionCAccess().getActionTypeFollowKeyword_0_3_0_0());
                    						

                    							if (current==null) {
                    								current = createModelElement(grammarAccess.getActionCRule());
                    							}
                    							setWithLastConsumed(current, "actionType", lv_actionType_11_0, "follow");
                    						

                    }


                    }

                    // InternalLIRAs.g:1558:5: ( (lv_agent_12_0= ruleAgent ) )
                    // InternalLIRAs.g:1559:6: (lv_agent_12_0= ruleAgent )
                    {
                    // InternalLIRAs.g:1559:6: (lv_agent_12_0= ruleAgent )
                    // InternalLIRAs.g:1560:7: lv_agent_12_0= ruleAgent
                    {

                    							newCompositeNode(grammarAccess.getActionCAccess().getAgentAgentParserRuleCall_0_3_1_0());
                    						
                    pushFollow(FOLLOW_32);
                    lv_agent_12_0=ruleAgent();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getActionCRule());
                    							}
                    							set(
                    								current,
                    								"agent",
                    								lv_agent_12_0,
                    								"it.polimi.LIRAs.Agent");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }

                    // InternalLIRAs.g:1577:5: (otherlv_13= '(' ( (lv_target_14_0= ruleTarget ) ) otherlv_15= ')' )?
                    int alt39=2;
                    int LA39_0 = input.LA(1);

                    if ( (LA39_0==37) ) {
                        alt39=1;
                    }
                    switch (alt39) {
                        case 1 :
                            // InternalLIRAs.g:1578:6: otherlv_13= '(' ( (lv_target_14_0= ruleTarget ) ) otherlv_15= ')'
                            {
                            otherlv_13=(Token)match(input,37,FOLLOW_9); 

                            						newLeafNode(otherlv_13, grammarAccess.getActionCAccess().getLeftParenthesisKeyword_0_3_2_0());
                            					
                            // InternalLIRAs.g:1582:6: ( (lv_target_14_0= ruleTarget ) )
                            // InternalLIRAs.g:1583:7: (lv_target_14_0= ruleTarget )
                            {
                            // InternalLIRAs.g:1583:7: (lv_target_14_0= ruleTarget )
                            // InternalLIRAs.g:1584:8: lv_target_14_0= ruleTarget
                            {

                            								newCompositeNode(grammarAccess.getActionCAccess().getTargetTargetParserRuleCall_0_3_2_1_0());
                            							
                            pushFollow(FOLLOW_29);
                            lv_target_14_0=ruleTarget();

                            state._fsp--;


                            								if (current==null) {
                            									current = createModelElementForParent(grammarAccess.getActionCRule());
                            								}
                            								set(
                            									current,
                            									"target",
                            									lv_target_14_0,
                            									"it.polimi.LIRAs.Target");
                            								afterParserOrEnumRuleCall();
                            							

                            }


                            }

                            otherlv_15=(Token)match(input,38,FOLLOW_30); 

                            						newLeafNode(otherlv_15, grammarAccess.getActionCAccess().getRightParenthesisKeyword_0_3_2_2());
                            					

                            }
                            break;

                    }


                    }


                    }
                    break;
                case 5 :
                    // InternalLIRAs.g:1608:4: ( ( (lv_actionType_16_0= 'receiveFrom' ) ) ( (lv_agent_17_0= ruleAgent ) ) )
                    {
                    // InternalLIRAs.g:1608:4: ( ( (lv_actionType_16_0= 'receiveFrom' ) ) ( (lv_agent_17_0= ruleAgent ) ) )
                    // InternalLIRAs.g:1609:5: ( (lv_actionType_16_0= 'receiveFrom' ) ) ( (lv_agent_17_0= ruleAgent ) )
                    {
                    // InternalLIRAs.g:1609:5: ( (lv_actionType_16_0= 'receiveFrom' ) )
                    // InternalLIRAs.g:1610:6: (lv_actionType_16_0= 'receiveFrom' )
                    {
                    // InternalLIRAs.g:1610:6: (lv_actionType_16_0= 'receiveFrom' )
                    // InternalLIRAs.g:1611:7: lv_actionType_16_0= 'receiveFrom'
                    {
                    lv_actionType_16_0=(Token)match(input,39,FOLLOW_9); 

                    							newLeafNode(lv_actionType_16_0, grammarAccess.getActionCAccess().getActionTypeReceiveFromKeyword_0_4_0_0());
                    						

                    							if (current==null) {
                    								current = createModelElement(grammarAccess.getActionCRule());
                    							}
                    							setWithLastConsumed(current, "actionType", lv_actionType_16_0, "receiveFrom");
                    						

                    }


                    }

                    // InternalLIRAs.g:1623:5: ( (lv_agent_17_0= ruleAgent ) )
                    // InternalLIRAs.g:1624:6: (lv_agent_17_0= ruleAgent )
                    {
                    // InternalLIRAs.g:1624:6: (lv_agent_17_0= ruleAgent )
                    // InternalLIRAs.g:1625:7: lv_agent_17_0= ruleAgent
                    {

                    							newCompositeNode(grammarAccess.getActionCAccess().getAgentAgentParserRuleCall_0_4_1_0());
                    						
                    pushFollow(FOLLOW_30);
                    lv_agent_17_0=ruleAgent();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getActionCRule());
                    							}
                    							set(
                    								current,
                    								"agent",
                    								lv_agent_17_0,
                    								"it.polimi.LIRAs.Agent");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }


                    }


                    }
                    break;

            }

            // InternalLIRAs.g:1644:3: ( (lv_conditionalStatement_18_0= ruleConditionalStatement ) )?
            int alt41=2;
            int LA41_0 = input.LA(1);

            if ( (LA41_0==25||LA41_0==27||LA41_0==29) ) {
                alt41=1;
            }
            switch (alt41) {
                case 1 :
                    // InternalLIRAs.g:1645:4: (lv_conditionalStatement_18_0= ruleConditionalStatement )
                    {
                    // InternalLIRAs.g:1645:4: (lv_conditionalStatement_18_0= ruleConditionalStatement )
                    // InternalLIRAs.g:1646:5: lv_conditionalStatement_18_0= ruleConditionalStatement
                    {

                    					newCompositeNode(grammarAccess.getActionCAccess().getConditionalStatementConditionalStatementParserRuleCall_1_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_conditionalStatement_18_0=ruleConditionalStatement();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getActionCRule());
                    					}
                    					add(
                    						current,
                    						"conditionalStatement",
                    						lv_conditionalStatement_18_0,
                    						"it.polimi.LIRAs.ConditionalStatement");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleActionC"


    // $ANTLR start "entryRuleCondition"
    // InternalLIRAs.g:1667:1: entryRuleCondition returns [EObject current=null] : iv_ruleCondition= ruleCondition EOF ;
    public final EObject entryRuleCondition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCondition = null;


        try {
            // InternalLIRAs.g:1667:50: (iv_ruleCondition= ruleCondition EOF )
            // InternalLIRAs.g:1668:2: iv_ruleCondition= ruleCondition EOF
            {
             newCompositeNode(grammarAccess.getConditionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCondition=ruleCondition();

            state._fsp--;

             current =iv_ruleCondition; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleCondition"


    // $ANTLR start "ruleCondition"
    // InternalLIRAs.g:1674:1: ruleCondition returns [EObject current=null] : ( ( (lv_atomicPredicate_0_0= ruleAtomicPredicate ) ) ( ( ( (lv_op_1_0= '&&' ) ) | ( (lv_op_2_0= '||' ) ) ) ( (lv_atomicPredicate_3_0= ruleAtomicPredicate ) ) )* ) ;
    public final EObject ruleCondition() throws RecognitionException {
        EObject current = null;

        Token lv_op_1_0=null;
        Token lv_op_2_0=null;
        EObject lv_atomicPredicate_0_0 = null;

        EObject lv_atomicPredicate_3_0 = null;



        	enterRule();

        try {
            // InternalLIRAs.g:1680:2: ( ( ( (lv_atomicPredicate_0_0= ruleAtomicPredicate ) ) ( ( ( (lv_op_1_0= '&&' ) ) | ( (lv_op_2_0= '||' ) ) ) ( (lv_atomicPredicate_3_0= ruleAtomicPredicate ) ) )* ) )
            // InternalLIRAs.g:1681:2: ( ( (lv_atomicPredicate_0_0= ruleAtomicPredicate ) ) ( ( ( (lv_op_1_0= '&&' ) ) | ( (lv_op_2_0= '||' ) ) ) ( (lv_atomicPredicate_3_0= ruleAtomicPredicate ) ) )* )
            {
            // InternalLIRAs.g:1681:2: ( ( (lv_atomicPredicate_0_0= ruleAtomicPredicate ) ) ( ( ( (lv_op_1_0= '&&' ) ) | ( (lv_op_2_0= '||' ) ) ) ( (lv_atomicPredicate_3_0= ruleAtomicPredicate ) ) )* )
            // InternalLIRAs.g:1682:3: ( (lv_atomicPredicate_0_0= ruleAtomicPredicate ) ) ( ( ( (lv_op_1_0= '&&' ) ) | ( (lv_op_2_0= '||' ) ) ) ( (lv_atomicPredicate_3_0= ruleAtomicPredicate ) ) )*
            {
            // InternalLIRAs.g:1682:3: ( (lv_atomicPredicate_0_0= ruleAtomicPredicate ) )
            // InternalLIRAs.g:1683:4: (lv_atomicPredicate_0_0= ruleAtomicPredicate )
            {
            // InternalLIRAs.g:1683:4: (lv_atomicPredicate_0_0= ruleAtomicPredicate )
            // InternalLIRAs.g:1684:5: lv_atomicPredicate_0_0= ruleAtomicPredicate
            {

            					newCompositeNode(grammarAccess.getConditionAccess().getAtomicPredicateAtomicPredicateParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_33);
            lv_atomicPredicate_0_0=ruleAtomicPredicate();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getConditionRule());
            					}
            					add(
            						current,
            						"atomicPredicate",
            						lv_atomicPredicate_0_0,
            						"it.polimi.LIRAs.AtomicPredicate");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalLIRAs.g:1701:3: ( ( ( (lv_op_1_0= '&&' ) ) | ( (lv_op_2_0= '||' ) ) ) ( (lv_atomicPredicate_3_0= ruleAtomicPredicate ) ) )*
            loop43:
            do {
                int alt43=2;
                int LA43_0 = input.LA(1);

                if ( ((LA43_0>=41 && LA43_0<=42)) ) {
                    alt43=1;
                }


                switch (alt43) {
            	case 1 :
            	    // InternalLIRAs.g:1702:4: ( ( (lv_op_1_0= '&&' ) ) | ( (lv_op_2_0= '||' ) ) ) ( (lv_atomicPredicate_3_0= ruleAtomicPredicate ) )
            	    {
            	    // InternalLIRAs.g:1702:4: ( ( (lv_op_1_0= '&&' ) ) | ( (lv_op_2_0= '||' ) ) )
            	    int alt42=2;
            	    int LA42_0 = input.LA(1);

            	    if ( (LA42_0==41) ) {
            	        alt42=1;
            	    }
            	    else if ( (LA42_0==42) ) {
            	        alt42=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 42, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt42) {
            	        case 1 :
            	            // InternalLIRAs.g:1703:5: ( (lv_op_1_0= '&&' ) )
            	            {
            	            // InternalLIRAs.g:1703:5: ( (lv_op_1_0= '&&' ) )
            	            // InternalLIRAs.g:1704:6: (lv_op_1_0= '&&' )
            	            {
            	            // InternalLIRAs.g:1704:6: (lv_op_1_0= '&&' )
            	            // InternalLIRAs.g:1705:7: lv_op_1_0= '&&'
            	            {
            	            lv_op_1_0=(Token)match(input,41,FOLLOW_20); 

            	            							newLeafNode(lv_op_1_0, grammarAccess.getConditionAccess().getOpAmpersandAmpersandKeyword_1_0_0_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getConditionRule());
            	            							}
            	            							addWithLastConsumed(current, "op", lv_op_1_0, "&&");
            	            						

            	            }


            	            }


            	            }
            	            break;
            	        case 2 :
            	            // InternalLIRAs.g:1718:5: ( (lv_op_2_0= '||' ) )
            	            {
            	            // InternalLIRAs.g:1718:5: ( (lv_op_2_0= '||' ) )
            	            // InternalLIRAs.g:1719:6: (lv_op_2_0= '||' )
            	            {
            	            // InternalLIRAs.g:1719:6: (lv_op_2_0= '||' )
            	            // InternalLIRAs.g:1720:7: lv_op_2_0= '||'
            	            {
            	            lv_op_2_0=(Token)match(input,42,FOLLOW_20); 

            	            							newLeafNode(lv_op_2_0, grammarAccess.getConditionAccess().getOpVerticalLineVerticalLineKeyword_1_0_1_0());
            	            						

            	            							if (current==null) {
            	            								current = createModelElement(grammarAccess.getConditionRule());
            	            							}
            	            							addWithLastConsumed(current, "op", lv_op_2_0, "||");
            	            						

            	            }


            	            }


            	            }
            	            break;

            	    }

            	    // InternalLIRAs.g:1733:4: ( (lv_atomicPredicate_3_0= ruleAtomicPredicate ) )
            	    // InternalLIRAs.g:1734:5: (lv_atomicPredicate_3_0= ruleAtomicPredicate )
            	    {
            	    // InternalLIRAs.g:1734:5: (lv_atomicPredicate_3_0= ruleAtomicPredicate )
            	    // InternalLIRAs.g:1735:6: lv_atomicPredicate_3_0= ruleAtomicPredicate
            	    {

            	    						newCompositeNode(grammarAccess.getConditionAccess().getAtomicPredicateAtomicPredicateParserRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_33);
            	    lv_atomicPredicate_3_0=ruleAtomicPredicate();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getConditionRule());
            	    						}
            	    						add(
            	    							current,
            	    							"atomicPredicate",
            	    							lv_atomicPredicate_3_0,
            	    							"it.polimi.LIRAs.AtomicPredicate");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop43;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleCondition"


    // $ANTLR start "entryRuleAtomicPredicate"
    // InternalLIRAs.g:1757:1: entryRuleAtomicPredicate returns [EObject current=null] : iv_ruleAtomicPredicate= ruleAtomicPredicate EOF ;
    public final EObject entryRuleAtomicPredicate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomicPredicate = null;


        try {
            // InternalLIRAs.g:1757:56: (iv_ruleAtomicPredicate= ruleAtomicPredicate EOF )
            // InternalLIRAs.g:1758:2: iv_ruleAtomicPredicate= ruleAtomicPredicate EOF
            {
             newCompositeNode(grammarAccess.getAtomicPredicateRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAtomicPredicate=ruleAtomicPredicate();

            state._fsp--;

             current =iv_ruleAtomicPredicate; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAtomicPredicate"


    // $ANTLR start "ruleAtomicPredicate"
    // InternalLIRAs.g:1764:1: ruleAtomicPredicate returns [EObject current=null] : ( () ( (lv_negated_1_0= '!' ) )? ( ( (lv_ap_2_0= 'tired' ) ) | ( ( (lv_ap_3_0= 'time' ) ) otherlv_4= '(' ( (lv_n_5_0= RULE_INT ) ) otherlv_6= ')' ) | ( ( (lv_ap_7_0= 'position' ) ) otherlv_8= '(' ( (lv_agent_9_0= ruleAgent ) ) otherlv_10= ',' ( (lv_location_11_0= ruleLocation ) ) otherlv_12= ')' ) | ( ( (lv_ap_13_0= 'possess' ) ) otherlv_14= '(' ( (lv_agent_15_0= ruleAgent ) ) otherlv_16= ',' ( (lv_resource_17_0= ruleResource ) ) otherlv_18= ')' ) | ( ( (lv_ap_19_0= 'busy' ) ) otherlv_20= '(' ( (lv_agent_21_0= ruleAgent ) ) otherlv_22= ')' ) | ( ( (lv_ap_23_0= 'still' ) ) otherlv_24= '(' ( (lv_agent_25_0= ruleAgent ) ) otherlv_26= ')' ) | ( (lv_relation_27_0= ruleRelation ) ) ) ) ;
    public final EObject ruleAtomicPredicate() throws RecognitionException {
        EObject current = null;

        Token lv_negated_1_0=null;
        Token lv_ap_2_0=null;
        Token lv_ap_3_0=null;
        Token otherlv_4=null;
        Token lv_n_5_0=null;
        Token otherlv_6=null;
        Token lv_ap_7_0=null;
        Token otherlv_8=null;
        Token otherlv_10=null;
        Token otherlv_12=null;
        Token lv_ap_13_0=null;
        Token otherlv_14=null;
        Token otherlv_16=null;
        Token otherlv_18=null;
        Token lv_ap_19_0=null;
        Token otherlv_20=null;
        Token otherlv_22=null;
        Token lv_ap_23_0=null;
        Token otherlv_24=null;
        Token otherlv_26=null;
        EObject lv_agent_9_0 = null;

        EObject lv_location_11_0 = null;

        EObject lv_agent_15_0 = null;

        EObject lv_resource_17_0 = null;

        EObject lv_agent_21_0 = null;

        EObject lv_agent_25_0 = null;

        EObject lv_relation_27_0 = null;



        	enterRule();

        try {
            // InternalLIRAs.g:1770:2: ( ( () ( (lv_negated_1_0= '!' ) )? ( ( (lv_ap_2_0= 'tired' ) ) | ( ( (lv_ap_3_0= 'time' ) ) otherlv_4= '(' ( (lv_n_5_0= RULE_INT ) ) otherlv_6= ')' ) | ( ( (lv_ap_7_0= 'position' ) ) otherlv_8= '(' ( (lv_agent_9_0= ruleAgent ) ) otherlv_10= ',' ( (lv_location_11_0= ruleLocation ) ) otherlv_12= ')' ) | ( ( (lv_ap_13_0= 'possess' ) ) otherlv_14= '(' ( (lv_agent_15_0= ruleAgent ) ) otherlv_16= ',' ( (lv_resource_17_0= ruleResource ) ) otherlv_18= ')' ) | ( ( (lv_ap_19_0= 'busy' ) ) otherlv_20= '(' ( (lv_agent_21_0= ruleAgent ) ) otherlv_22= ')' ) | ( ( (lv_ap_23_0= 'still' ) ) otherlv_24= '(' ( (lv_agent_25_0= ruleAgent ) ) otherlv_26= ')' ) | ( (lv_relation_27_0= ruleRelation ) ) ) ) )
            // InternalLIRAs.g:1771:2: ( () ( (lv_negated_1_0= '!' ) )? ( ( (lv_ap_2_0= 'tired' ) ) | ( ( (lv_ap_3_0= 'time' ) ) otherlv_4= '(' ( (lv_n_5_0= RULE_INT ) ) otherlv_6= ')' ) | ( ( (lv_ap_7_0= 'position' ) ) otherlv_8= '(' ( (lv_agent_9_0= ruleAgent ) ) otherlv_10= ',' ( (lv_location_11_0= ruleLocation ) ) otherlv_12= ')' ) | ( ( (lv_ap_13_0= 'possess' ) ) otherlv_14= '(' ( (lv_agent_15_0= ruleAgent ) ) otherlv_16= ',' ( (lv_resource_17_0= ruleResource ) ) otherlv_18= ')' ) | ( ( (lv_ap_19_0= 'busy' ) ) otherlv_20= '(' ( (lv_agent_21_0= ruleAgent ) ) otherlv_22= ')' ) | ( ( (lv_ap_23_0= 'still' ) ) otherlv_24= '(' ( (lv_agent_25_0= ruleAgent ) ) otherlv_26= ')' ) | ( (lv_relation_27_0= ruleRelation ) ) ) )
            {
            // InternalLIRAs.g:1771:2: ( () ( (lv_negated_1_0= '!' ) )? ( ( (lv_ap_2_0= 'tired' ) ) | ( ( (lv_ap_3_0= 'time' ) ) otherlv_4= '(' ( (lv_n_5_0= RULE_INT ) ) otherlv_6= ')' ) | ( ( (lv_ap_7_0= 'position' ) ) otherlv_8= '(' ( (lv_agent_9_0= ruleAgent ) ) otherlv_10= ',' ( (lv_location_11_0= ruleLocation ) ) otherlv_12= ')' ) | ( ( (lv_ap_13_0= 'possess' ) ) otherlv_14= '(' ( (lv_agent_15_0= ruleAgent ) ) otherlv_16= ',' ( (lv_resource_17_0= ruleResource ) ) otherlv_18= ')' ) | ( ( (lv_ap_19_0= 'busy' ) ) otherlv_20= '(' ( (lv_agent_21_0= ruleAgent ) ) otherlv_22= ')' ) | ( ( (lv_ap_23_0= 'still' ) ) otherlv_24= '(' ( (lv_agent_25_0= ruleAgent ) ) otherlv_26= ')' ) | ( (lv_relation_27_0= ruleRelation ) ) ) )
            // InternalLIRAs.g:1772:3: () ( (lv_negated_1_0= '!' ) )? ( ( (lv_ap_2_0= 'tired' ) ) | ( ( (lv_ap_3_0= 'time' ) ) otherlv_4= '(' ( (lv_n_5_0= RULE_INT ) ) otherlv_6= ')' ) | ( ( (lv_ap_7_0= 'position' ) ) otherlv_8= '(' ( (lv_agent_9_0= ruleAgent ) ) otherlv_10= ',' ( (lv_location_11_0= ruleLocation ) ) otherlv_12= ')' ) | ( ( (lv_ap_13_0= 'possess' ) ) otherlv_14= '(' ( (lv_agent_15_0= ruleAgent ) ) otherlv_16= ',' ( (lv_resource_17_0= ruleResource ) ) otherlv_18= ')' ) | ( ( (lv_ap_19_0= 'busy' ) ) otherlv_20= '(' ( (lv_agent_21_0= ruleAgent ) ) otherlv_22= ')' ) | ( ( (lv_ap_23_0= 'still' ) ) otherlv_24= '(' ( (lv_agent_25_0= ruleAgent ) ) otherlv_26= ')' ) | ( (lv_relation_27_0= ruleRelation ) ) )
            {
            // InternalLIRAs.g:1772:3: ()
            // InternalLIRAs.g:1773:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getAtomicPredicateAccess().getAtomicPredicateAction_0(),
            					current);
            			

            }

            // InternalLIRAs.g:1779:3: ( (lv_negated_1_0= '!' ) )?
            int alt44=2;
            int LA44_0 = input.LA(1);

            if ( (LA44_0==43) ) {
                alt44=1;
            }
            switch (alt44) {
                case 1 :
                    // InternalLIRAs.g:1780:4: (lv_negated_1_0= '!' )
                    {
                    // InternalLIRAs.g:1780:4: (lv_negated_1_0= '!' )
                    // InternalLIRAs.g:1781:5: lv_negated_1_0= '!'
                    {
                    lv_negated_1_0=(Token)match(input,43,FOLLOW_20); 

                    					newLeafNode(lv_negated_1_0, grammarAccess.getAtomicPredicateAccess().getNegatedExclamationMarkKeyword_1_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getAtomicPredicateRule());
                    					}
                    					setWithLastConsumed(current, "negated", lv_negated_1_0 != null, "!");
                    				

                    }


                    }
                    break;

            }

            // InternalLIRAs.g:1793:3: ( ( (lv_ap_2_0= 'tired' ) ) | ( ( (lv_ap_3_0= 'time' ) ) otherlv_4= '(' ( (lv_n_5_0= RULE_INT ) ) otherlv_6= ')' ) | ( ( (lv_ap_7_0= 'position' ) ) otherlv_8= '(' ( (lv_agent_9_0= ruleAgent ) ) otherlv_10= ',' ( (lv_location_11_0= ruleLocation ) ) otherlv_12= ')' ) | ( ( (lv_ap_13_0= 'possess' ) ) otherlv_14= '(' ( (lv_agent_15_0= ruleAgent ) ) otherlv_16= ',' ( (lv_resource_17_0= ruleResource ) ) otherlv_18= ')' ) | ( ( (lv_ap_19_0= 'busy' ) ) otherlv_20= '(' ( (lv_agent_21_0= ruleAgent ) ) otherlv_22= ')' ) | ( ( (lv_ap_23_0= 'still' ) ) otherlv_24= '(' ( (lv_agent_25_0= ruleAgent ) ) otherlv_26= ')' ) | ( (lv_relation_27_0= ruleRelation ) ) )
            int alt45=7;
            switch ( input.LA(1) ) {
            case 44:
                {
                alt45=1;
                }
                break;
            case 45:
                {
                alt45=2;
                }
                break;
            case 46:
                {
                alt45=3;
                }
                break;
            case 47:
                {
                alt45=4;
                }
                break;
            case 48:
                {
                alt45=5;
                }
                break;
            case 49:
                {
                alt45=6;
                }
                break;
            case 50:
            case 57:
                {
                alt45=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 45, 0, input);

                throw nvae;
            }

            switch (alt45) {
                case 1 :
                    // InternalLIRAs.g:1794:4: ( (lv_ap_2_0= 'tired' ) )
                    {
                    // InternalLIRAs.g:1794:4: ( (lv_ap_2_0= 'tired' ) )
                    // InternalLIRAs.g:1795:5: (lv_ap_2_0= 'tired' )
                    {
                    // InternalLIRAs.g:1795:5: (lv_ap_2_0= 'tired' )
                    // InternalLIRAs.g:1796:6: lv_ap_2_0= 'tired'
                    {
                    lv_ap_2_0=(Token)match(input,44,FOLLOW_2); 

                    						newLeafNode(lv_ap_2_0, grammarAccess.getAtomicPredicateAccess().getApTiredKeyword_2_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAtomicPredicateRule());
                    						}
                    						setWithLastConsumed(current, "ap", lv_ap_2_0, "tired");
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalLIRAs.g:1809:4: ( ( (lv_ap_3_0= 'time' ) ) otherlv_4= '(' ( (lv_n_5_0= RULE_INT ) ) otherlv_6= ')' )
                    {
                    // InternalLIRAs.g:1809:4: ( ( (lv_ap_3_0= 'time' ) ) otherlv_4= '(' ( (lv_n_5_0= RULE_INT ) ) otherlv_6= ')' )
                    // InternalLIRAs.g:1810:5: ( (lv_ap_3_0= 'time' ) ) otherlv_4= '(' ( (lv_n_5_0= RULE_INT ) ) otherlv_6= ')'
                    {
                    // InternalLIRAs.g:1810:5: ( (lv_ap_3_0= 'time' ) )
                    // InternalLIRAs.g:1811:6: (lv_ap_3_0= 'time' )
                    {
                    // InternalLIRAs.g:1811:6: (lv_ap_3_0= 'time' )
                    // InternalLIRAs.g:1812:7: lv_ap_3_0= 'time'
                    {
                    lv_ap_3_0=(Token)match(input,45,FOLLOW_34); 

                    							newLeafNode(lv_ap_3_0, grammarAccess.getAtomicPredicateAccess().getApTimeKeyword_2_1_0_0());
                    						

                    							if (current==null) {
                    								current = createModelElement(grammarAccess.getAtomicPredicateRule());
                    							}
                    							setWithLastConsumed(current, "ap", lv_ap_3_0, "time");
                    						

                    }


                    }

                    otherlv_4=(Token)match(input,37,FOLLOW_17); 

                    					newLeafNode(otherlv_4, grammarAccess.getAtomicPredicateAccess().getLeftParenthesisKeyword_2_1_1());
                    				
                    // InternalLIRAs.g:1828:5: ( (lv_n_5_0= RULE_INT ) )
                    // InternalLIRAs.g:1829:6: (lv_n_5_0= RULE_INT )
                    {
                    // InternalLIRAs.g:1829:6: (lv_n_5_0= RULE_INT )
                    // InternalLIRAs.g:1830:7: lv_n_5_0= RULE_INT
                    {
                    lv_n_5_0=(Token)match(input,RULE_INT,FOLLOW_29); 

                    							newLeafNode(lv_n_5_0, grammarAccess.getAtomicPredicateAccess().getNINTTerminalRuleCall_2_1_2_0());
                    						

                    							if (current==null) {
                    								current = createModelElement(grammarAccess.getAtomicPredicateRule());
                    							}
                    							setWithLastConsumed(
                    								current,
                    								"n",
                    								lv_n_5_0,
                    								"org.eclipse.xtext.common.Terminals.INT");
                    						

                    }


                    }

                    otherlv_6=(Token)match(input,38,FOLLOW_2); 

                    					newLeafNode(otherlv_6, grammarAccess.getAtomicPredicateAccess().getRightParenthesisKeyword_2_1_3());
                    				

                    }


                    }
                    break;
                case 3 :
                    // InternalLIRAs.g:1852:4: ( ( (lv_ap_7_0= 'position' ) ) otherlv_8= '(' ( (lv_agent_9_0= ruleAgent ) ) otherlv_10= ',' ( (lv_location_11_0= ruleLocation ) ) otherlv_12= ')' )
                    {
                    // InternalLIRAs.g:1852:4: ( ( (lv_ap_7_0= 'position' ) ) otherlv_8= '(' ( (lv_agent_9_0= ruleAgent ) ) otherlv_10= ',' ( (lv_location_11_0= ruleLocation ) ) otherlv_12= ')' )
                    // InternalLIRAs.g:1853:5: ( (lv_ap_7_0= 'position' ) ) otherlv_8= '(' ( (lv_agent_9_0= ruleAgent ) ) otherlv_10= ',' ( (lv_location_11_0= ruleLocation ) ) otherlv_12= ')'
                    {
                    // InternalLIRAs.g:1853:5: ( (lv_ap_7_0= 'position' ) )
                    // InternalLIRAs.g:1854:6: (lv_ap_7_0= 'position' )
                    {
                    // InternalLIRAs.g:1854:6: (lv_ap_7_0= 'position' )
                    // InternalLIRAs.g:1855:7: lv_ap_7_0= 'position'
                    {
                    lv_ap_7_0=(Token)match(input,46,FOLLOW_34); 

                    							newLeafNode(lv_ap_7_0, grammarAccess.getAtomicPredicateAccess().getApPositionKeyword_2_2_0_0());
                    						

                    							if (current==null) {
                    								current = createModelElement(grammarAccess.getAtomicPredicateRule());
                    							}
                    							setWithLastConsumed(current, "ap", lv_ap_7_0, "position");
                    						

                    }


                    }

                    otherlv_8=(Token)match(input,37,FOLLOW_9); 

                    					newLeafNode(otherlv_8, grammarAccess.getAtomicPredicateAccess().getLeftParenthesisKeyword_2_2_1());
                    				
                    // InternalLIRAs.g:1871:5: ( (lv_agent_9_0= ruleAgent ) )
                    // InternalLIRAs.g:1872:6: (lv_agent_9_0= ruleAgent )
                    {
                    // InternalLIRAs.g:1872:6: (lv_agent_9_0= ruleAgent )
                    // InternalLIRAs.g:1873:7: lv_agent_9_0= ruleAgent
                    {

                    							newCompositeNode(grammarAccess.getAtomicPredicateAccess().getAgentAgentParserRuleCall_2_2_2_0());
                    						
                    pushFollow(FOLLOW_35);
                    lv_agent_9_0=ruleAgent();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getAtomicPredicateRule());
                    							}
                    							set(
                    								current,
                    								"agent",
                    								lv_agent_9_0,
                    								"it.polimi.LIRAs.Agent");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }

                    otherlv_10=(Token)match(input,14,FOLLOW_9); 

                    					newLeafNode(otherlv_10, grammarAccess.getAtomicPredicateAccess().getCommaKeyword_2_2_3());
                    				
                    // InternalLIRAs.g:1894:5: ( (lv_location_11_0= ruleLocation ) )
                    // InternalLIRAs.g:1895:6: (lv_location_11_0= ruleLocation )
                    {
                    // InternalLIRAs.g:1895:6: (lv_location_11_0= ruleLocation )
                    // InternalLIRAs.g:1896:7: lv_location_11_0= ruleLocation
                    {

                    							newCompositeNode(grammarAccess.getAtomicPredicateAccess().getLocationLocationParserRuleCall_2_2_4_0());
                    						
                    pushFollow(FOLLOW_29);
                    lv_location_11_0=ruleLocation();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getAtomicPredicateRule());
                    							}
                    							set(
                    								current,
                    								"location",
                    								lv_location_11_0,
                    								"it.polimi.LIRAs.Location");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }

                    otherlv_12=(Token)match(input,38,FOLLOW_2); 

                    					newLeafNode(otherlv_12, grammarAccess.getAtomicPredicateAccess().getRightParenthesisKeyword_2_2_5());
                    				

                    }


                    }
                    break;
                case 4 :
                    // InternalLIRAs.g:1919:4: ( ( (lv_ap_13_0= 'possess' ) ) otherlv_14= '(' ( (lv_agent_15_0= ruleAgent ) ) otherlv_16= ',' ( (lv_resource_17_0= ruleResource ) ) otherlv_18= ')' )
                    {
                    // InternalLIRAs.g:1919:4: ( ( (lv_ap_13_0= 'possess' ) ) otherlv_14= '(' ( (lv_agent_15_0= ruleAgent ) ) otherlv_16= ',' ( (lv_resource_17_0= ruleResource ) ) otherlv_18= ')' )
                    // InternalLIRAs.g:1920:5: ( (lv_ap_13_0= 'possess' ) ) otherlv_14= '(' ( (lv_agent_15_0= ruleAgent ) ) otherlv_16= ',' ( (lv_resource_17_0= ruleResource ) ) otherlv_18= ')'
                    {
                    // InternalLIRAs.g:1920:5: ( (lv_ap_13_0= 'possess' ) )
                    // InternalLIRAs.g:1921:6: (lv_ap_13_0= 'possess' )
                    {
                    // InternalLIRAs.g:1921:6: (lv_ap_13_0= 'possess' )
                    // InternalLIRAs.g:1922:7: lv_ap_13_0= 'possess'
                    {
                    lv_ap_13_0=(Token)match(input,47,FOLLOW_34); 

                    							newLeafNode(lv_ap_13_0, grammarAccess.getAtomicPredicateAccess().getApPossessKeyword_2_3_0_0());
                    						

                    							if (current==null) {
                    								current = createModelElement(grammarAccess.getAtomicPredicateRule());
                    							}
                    							setWithLastConsumed(current, "ap", lv_ap_13_0, "possess");
                    						

                    }


                    }

                    otherlv_14=(Token)match(input,37,FOLLOW_9); 

                    					newLeafNode(otherlv_14, grammarAccess.getAtomicPredicateAccess().getLeftParenthesisKeyword_2_3_1());
                    				
                    // InternalLIRAs.g:1938:5: ( (lv_agent_15_0= ruleAgent ) )
                    // InternalLIRAs.g:1939:6: (lv_agent_15_0= ruleAgent )
                    {
                    // InternalLIRAs.g:1939:6: (lv_agent_15_0= ruleAgent )
                    // InternalLIRAs.g:1940:7: lv_agent_15_0= ruleAgent
                    {

                    							newCompositeNode(grammarAccess.getAtomicPredicateAccess().getAgentAgentParserRuleCall_2_3_2_0());
                    						
                    pushFollow(FOLLOW_35);
                    lv_agent_15_0=ruleAgent();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getAtomicPredicateRule());
                    							}
                    							set(
                    								current,
                    								"agent",
                    								lv_agent_15_0,
                    								"it.polimi.LIRAs.Agent");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }

                    otherlv_16=(Token)match(input,14,FOLLOW_9); 

                    					newLeafNode(otherlv_16, grammarAccess.getAtomicPredicateAccess().getCommaKeyword_2_3_3());
                    				
                    // InternalLIRAs.g:1961:5: ( (lv_resource_17_0= ruleResource ) )
                    // InternalLIRAs.g:1962:6: (lv_resource_17_0= ruleResource )
                    {
                    // InternalLIRAs.g:1962:6: (lv_resource_17_0= ruleResource )
                    // InternalLIRAs.g:1963:7: lv_resource_17_0= ruleResource
                    {

                    							newCompositeNode(grammarAccess.getAtomicPredicateAccess().getResourceResourceParserRuleCall_2_3_4_0());
                    						
                    pushFollow(FOLLOW_29);
                    lv_resource_17_0=ruleResource();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getAtomicPredicateRule());
                    							}
                    							set(
                    								current,
                    								"resource",
                    								lv_resource_17_0,
                    								"it.polimi.LIRAs.Resource");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }

                    otherlv_18=(Token)match(input,38,FOLLOW_2); 

                    					newLeafNode(otherlv_18, grammarAccess.getAtomicPredicateAccess().getRightParenthesisKeyword_2_3_5());
                    				

                    }


                    }
                    break;
                case 5 :
                    // InternalLIRAs.g:1986:4: ( ( (lv_ap_19_0= 'busy' ) ) otherlv_20= '(' ( (lv_agent_21_0= ruleAgent ) ) otherlv_22= ')' )
                    {
                    // InternalLIRAs.g:1986:4: ( ( (lv_ap_19_0= 'busy' ) ) otherlv_20= '(' ( (lv_agent_21_0= ruleAgent ) ) otherlv_22= ')' )
                    // InternalLIRAs.g:1987:5: ( (lv_ap_19_0= 'busy' ) ) otherlv_20= '(' ( (lv_agent_21_0= ruleAgent ) ) otherlv_22= ')'
                    {
                    // InternalLIRAs.g:1987:5: ( (lv_ap_19_0= 'busy' ) )
                    // InternalLIRAs.g:1988:6: (lv_ap_19_0= 'busy' )
                    {
                    // InternalLIRAs.g:1988:6: (lv_ap_19_0= 'busy' )
                    // InternalLIRAs.g:1989:7: lv_ap_19_0= 'busy'
                    {
                    lv_ap_19_0=(Token)match(input,48,FOLLOW_34); 

                    							newLeafNode(lv_ap_19_0, grammarAccess.getAtomicPredicateAccess().getApBusyKeyword_2_4_0_0());
                    						

                    							if (current==null) {
                    								current = createModelElement(grammarAccess.getAtomicPredicateRule());
                    							}
                    							setWithLastConsumed(current, "ap", lv_ap_19_0, "busy");
                    						

                    }


                    }

                    otherlv_20=(Token)match(input,37,FOLLOW_9); 

                    					newLeafNode(otherlv_20, grammarAccess.getAtomicPredicateAccess().getLeftParenthesisKeyword_2_4_1());
                    				
                    // InternalLIRAs.g:2005:5: ( (lv_agent_21_0= ruleAgent ) )
                    // InternalLIRAs.g:2006:6: (lv_agent_21_0= ruleAgent )
                    {
                    // InternalLIRAs.g:2006:6: (lv_agent_21_0= ruleAgent )
                    // InternalLIRAs.g:2007:7: lv_agent_21_0= ruleAgent
                    {

                    							newCompositeNode(grammarAccess.getAtomicPredicateAccess().getAgentAgentParserRuleCall_2_4_2_0());
                    						
                    pushFollow(FOLLOW_29);
                    lv_agent_21_0=ruleAgent();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getAtomicPredicateRule());
                    							}
                    							set(
                    								current,
                    								"agent",
                    								lv_agent_21_0,
                    								"it.polimi.LIRAs.Agent");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }

                    otherlv_22=(Token)match(input,38,FOLLOW_2); 

                    					newLeafNode(otherlv_22, grammarAccess.getAtomicPredicateAccess().getRightParenthesisKeyword_2_4_3());
                    				

                    }


                    }
                    break;
                case 6 :
                    // InternalLIRAs.g:2030:4: ( ( (lv_ap_23_0= 'still' ) ) otherlv_24= '(' ( (lv_agent_25_0= ruleAgent ) ) otherlv_26= ')' )
                    {
                    // InternalLIRAs.g:2030:4: ( ( (lv_ap_23_0= 'still' ) ) otherlv_24= '(' ( (lv_agent_25_0= ruleAgent ) ) otherlv_26= ')' )
                    // InternalLIRAs.g:2031:5: ( (lv_ap_23_0= 'still' ) ) otherlv_24= '(' ( (lv_agent_25_0= ruleAgent ) ) otherlv_26= ')'
                    {
                    // InternalLIRAs.g:2031:5: ( (lv_ap_23_0= 'still' ) )
                    // InternalLIRAs.g:2032:6: (lv_ap_23_0= 'still' )
                    {
                    // InternalLIRAs.g:2032:6: (lv_ap_23_0= 'still' )
                    // InternalLIRAs.g:2033:7: lv_ap_23_0= 'still'
                    {
                    lv_ap_23_0=(Token)match(input,49,FOLLOW_34); 

                    							newLeafNode(lv_ap_23_0, grammarAccess.getAtomicPredicateAccess().getApStillKeyword_2_5_0_0());
                    						

                    							if (current==null) {
                    								current = createModelElement(grammarAccess.getAtomicPredicateRule());
                    							}
                    							setWithLastConsumed(current, "ap", lv_ap_23_0, "still");
                    						

                    }


                    }

                    otherlv_24=(Token)match(input,37,FOLLOW_9); 

                    					newLeafNode(otherlv_24, grammarAccess.getAtomicPredicateAccess().getLeftParenthesisKeyword_2_5_1());
                    				
                    // InternalLIRAs.g:2049:5: ( (lv_agent_25_0= ruleAgent ) )
                    // InternalLIRAs.g:2050:6: (lv_agent_25_0= ruleAgent )
                    {
                    // InternalLIRAs.g:2050:6: (lv_agent_25_0= ruleAgent )
                    // InternalLIRAs.g:2051:7: lv_agent_25_0= ruleAgent
                    {

                    							newCompositeNode(grammarAccess.getAtomicPredicateAccess().getAgentAgentParserRuleCall_2_5_2_0());
                    						
                    pushFollow(FOLLOW_29);
                    lv_agent_25_0=ruleAgent();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getAtomicPredicateRule());
                    							}
                    							set(
                    								current,
                    								"agent",
                    								lv_agent_25_0,
                    								"it.polimi.LIRAs.Agent");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }

                    otherlv_26=(Token)match(input,38,FOLLOW_2); 

                    					newLeafNode(otherlv_26, grammarAccess.getAtomicPredicateAccess().getRightParenthesisKeyword_2_5_3());
                    				

                    }


                    }
                    break;
                case 7 :
                    // InternalLIRAs.g:2074:4: ( (lv_relation_27_0= ruleRelation ) )
                    {
                    // InternalLIRAs.g:2074:4: ( (lv_relation_27_0= ruleRelation ) )
                    // InternalLIRAs.g:2075:5: (lv_relation_27_0= ruleRelation )
                    {
                    // InternalLIRAs.g:2075:5: (lv_relation_27_0= ruleRelation )
                    // InternalLIRAs.g:2076:6: lv_relation_27_0= ruleRelation
                    {

                    						newCompositeNode(grammarAccess.getAtomicPredicateAccess().getRelationRelationParserRuleCall_2_6_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_relation_27_0=ruleRelation();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getAtomicPredicateRule());
                    						}
                    						set(
                    							current,
                    							"relation",
                    							lv_relation_27_0,
                    							"it.polimi.LIRAs.Relation");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAtomicPredicate"


    // $ANTLR start "entryRuleRelation"
    // InternalLIRAs.g:2098:1: entryRuleRelation returns [EObject current=null] : iv_ruleRelation= ruleRelation EOF ;
    public final EObject entryRuleRelation() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRelation = null;


        try {
            // InternalLIRAs.g:2098:49: (iv_ruleRelation= ruleRelation EOF )
            // InternalLIRAs.g:2099:2: iv_ruleRelation= ruleRelation EOF
            {
             newCompositeNode(grammarAccess.getRelationRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleRelation=ruleRelation();

            state._fsp--;

             current =iv_ruleRelation; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleRelation"


    // $ANTLR start "ruleRelation"
    // InternalLIRAs.g:2105:1: ruleRelation returns [EObject current=null] : ( (otherlv_0= 'dist' otherlv_1= '(' ( (lv_agent_2_0= ruleAgent ) ) otherlv_3= ',' ( (lv_agent_4_0= ruleAgent ) ) otherlv_5= ')' (otherlv_6= '>' | otherlv_7= '<' | otherlv_8= '>=' | otherlv_9= '<=' | otherlv_10= '==' | otherlv_11= '!=' ) ( (lv_th_12_0= RULE_INT ) ) ) | (otherlv_13= 'soc' otherlv_14= '(' ( (lv_agent_15_0= ruleAgent ) ) otherlv_16= ')' (otherlv_17= '>' | otherlv_18= '<' | otherlv_19= '>=' | otherlv_20= '<=' | otherlv_21= '==' | otherlv_22= '!=' ) ( (lv_th_23_0= RULE_INT ) ) ) ) ;
    public final EObject ruleRelation() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        Token otherlv_7=null;
        Token otherlv_8=null;
        Token otherlv_9=null;
        Token otherlv_10=null;
        Token otherlv_11=null;
        Token lv_th_12_0=null;
        Token otherlv_13=null;
        Token otherlv_14=null;
        Token otherlv_16=null;
        Token otherlv_17=null;
        Token otherlv_18=null;
        Token otherlv_19=null;
        Token otherlv_20=null;
        Token otherlv_21=null;
        Token otherlv_22=null;
        Token lv_th_23_0=null;
        EObject lv_agent_2_0 = null;

        EObject lv_agent_4_0 = null;

        EObject lv_agent_15_0 = null;



        	enterRule();

        try {
            // InternalLIRAs.g:2111:2: ( ( (otherlv_0= 'dist' otherlv_1= '(' ( (lv_agent_2_0= ruleAgent ) ) otherlv_3= ',' ( (lv_agent_4_0= ruleAgent ) ) otherlv_5= ')' (otherlv_6= '>' | otherlv_7= '<' | otherlv_8= '>=' | otherlv_9= '<=' | otherlv_10= '==' | otherlv_11= '!=' ) ( (lv_th_12_0= RULE_INT ) ) ) | (otherlv_13= 'soc' otherlv_14= '(' ( (lv_agent_15_0= ruleAgent ) ) otherlv_16= ')' (otherlv_17= '>' | otherlv_18= '<' | otherlv_19= '>=' | otherlv_20= '<=' | otherlv_21= '==' | otherlv_22= '!=' ) ( (lv_th_23_0= RULE_INT ) ) ) ) )
            // InternalLIRAs.g:2112:2: ( (otherlv_0= 'dist' otherlv_1= '(' ( (lv_agent_2_0= ruleAgent ) ) otherlv_3= ',' ( (lv_agent_4_0= ruleAgent ) ) otherlv_5= ')' (otherlv_6= '>' | otherlv_7= '<' | otherlv_8= '>=' | otherlv_9= '<=' | otherlv_10= '==' | otherlv_11= '!=' ) ( (lv_th_12_0= RULE_INT ) ) ) | (otherlv_13= 'soc' otherlv_14= '(' ( (lv_agent_15_0= ruleAgent ) ) otherlv_16= ')' (otherlv_17= '>' | otherlv_18= '<' | otherlv_19= '>=' | otherlv_20= '<=' | otherlv_21= '==' | otherlv_22= '!=' ) ( (lv_th_23_0= RULE_INT ) ) ) )
            {
            // InternalLIRAs.g:2112:2: ( (otherlv_0= 'dist' otherlv_1= '(' ( (lv_agent_2_0= ruleAgent ) ) otherlv_3= ',' ( (lv_agent_4_0= ruleAgent ) ) otherlv_5= ')' (otherlv_6= '>' | otherlv_7= '<' | otherlv_8= '>=' | otherlv_9= '<=' | otherlv_10= '==' | otherlv_11= '!=' ) ( (lv_th_12_0= RULE_INT ) ) ) | (otherlv_13= 'soc' otherlv_14= '(' ( (lv_agent_15_0= ruleAgent ) ) otherlv_16= ')' (otherlv_17= '>' | otherlv_18= '<' | otherlv_19= '>=' | otherlv_20= '<=' | otherlv_21= '==' | otherlv_22= '!=' ) ( (lv_th_23_0= RULE_INT ) ) ) )
            int alt48=2;
            int LA48_0 = input.LA(1);

            if ( (LA48_0==50) ) {
                alt48=1;
            }
            else if ( (LA48_0==57) ) {
                alt48=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 48, 0, input);

                throw nvae;
            }
            switch (alt48) {
                case 1 :
                    // InternalLIRAs.g:2113:3: (otherlv_0= 'dist' otherlv_1= '(' ( (lv_agent_2_0= ruleAgent ) ) otherlv_3= ',' ( (lv_agent_4_0= ruleAgent ) ) otherlv_5= ')' (otherlv_6= '>' | otherlv_7= '<' | otherlv_8= '>=' | otherlv_9= '<=' | otherlv_10= '==' | otherlv_11= '!=' ) ( (lv_th_12_0= RULE_INT ) ) )
                    {
                    // InternalLIRAs.g:2113:3: (otherlv_0= 'dist' otherlv_1= '(' ( (lv_agent_2_0= ruleAgent ) ) otherlv_3= ',' ( (lv_agent_4_0= ruleAgent ) ) otherlv_5= ')' (otherlv_6= '>' | otherlv_7= '<' | otherlv_8= '>=' | otherlv_9= '<=' | otherlv_10= '==' | otherlv_11= '!=' ) ( (lv_th_12_0= RULE_INT ) ) )
                    // InternalLIRAs.g:2114:4: otherlv_0= 'dist' otherlv_1= '(' ( (lv_agent_2_0= ruleAgent ) ) otherlv_3= ',' ( (lv_agent_4_0= ruleAgent ) ) otherlv_5= ')' (otherlv_6= '>' | otherlv_7= '<' | otherlv_8= '>=' | otherlv_9= '<=' | otherlv_10= '==' | otherlv_11= '!=' ) ( (lv_th_12_0= RULE_INT ) )
                    {
                    otherlv_0=(Token)match(input,50,FOLLOW_34); 

                    				newLeafNode(otherlv_0, grammarAccess.getRelationAccess().getDistKeyword_0_0());
                    			
                    otherlv_1=(Token)match(input,37,FOLLOW_9); 

                    				newLeafNode(otherlv_1, grammarAccess.getRelationAccess().getLeftParenthesisKeyword_0_1());
                    			
                    // InternalLIRAs.g:2122:4: ( (lv_agent_2_0= ruleAgent ) )
                    // InternalLIRAs.g:2123:5: (lv_agent_2_0= ruleAgent )
                    {
                    // InternalLIRAs.g:2123:5: (lv_agent_2_0= ruleAgent )
                    // InternalLIRAs.g:2124:6: lv_agent_2_0= ruleAgent
                    {

                    						newCompositeNode(grammarAccess.getRelationAccess().getAgentAgentParserRuleCall_0_2_0());
                    					
                    pushFollow(FOLLOW_35);
                    lv_agent_2_0=ruleAgent();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getRelationRule());
                    						}
                    						add(
                    							current,
                    							"agent",
                    							lv_agent_2_0,
                    							"it.polimi.LIRAs.Agent");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    otherlv_3=(Token)match(input,14,FOLLOW_9); 

                    				newLeafNode(otherlv_3, grammarAccess.getRelationAccess().getCommaKeyword_0_3());
                    			
                    // InternalLIRAs.g:2145:4: ( (lv_agent_4_0= ruleAgent ) )
                    // InternalLIRAs.g:2146:5: (lv_agent_4_0= ruleAgent )
                    {
                    // InternalLIRAs.g:2146:5: (lv_agent_4_0= ruleAgent )
                    // InternalLIRAs.g:2147:6: lv_agent_4_0= ruleAgent
                    {

                    						newCompositeNode(grammarAccess.getRelationAccess().getAgentAgentParserRuleCall_0_4_0());
                    					
                    pushFollow(FOLLOW_29);
                    lv_agent_4_0=ruleAgent();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getRelationRule());
                    						}
                    						add(
                    							current,
                    							"agent",
                    							lv_agent_4_0,
                    							"it.polimi.LIRAs.Agent");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    otherlv_5=(Token)match(input,38,FOLLOW_36); 

                    				newLeafNode(otherlv_5, grammarAccess.getRelationAccess().getRightParenthesisKeyword_0_5());
                    			
                    // InternalLIRAs.g:2168:4: (otherlv_6= '>' | otherlv_7= '<' | otherlv_8= '>=' | otherlv_9= '<=' | otherlv_10= '==' | otherlv_11= '!=' )
                    int alt46=6;
                    switch ( input.LA(1) ) {
                    case 51:
                        {
                        alt46=1;
                        }
                        break;
                    case 52:
                        {
                        alt46=2;
                        }
                        break;
                    case 53:
                        {
                        alt46=3;
                        }
                        break;
                    case 54:
                        {
                        alt46=4;
                        }
                        break;
                    case 55:
                        {
                        alt46=5;
                        }
                        break;
                    case 56:
                        {
                        alt46=6;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 46, 0, input);

                        throw nvae;
                    }

                    switch (alt46) {
                        case 1 :
                            // InternalLIRAs.g:2169:5: otherlv_6= '>'
                            {
                            otherlv_6=(Token)match(input,51,FOLLOW_17); 

                            					newLeafNode(otherlv_6, grammarAccess.getRelationAccess().getGreaterThanSignKeyword_0_6_0());
                            				

                            }
                            break;
                        case 2 :
                            // InternalLIRAs.g:2174:5: otherlv_7= '<'
                            {
                            otherlv_7=(Token)match(input,52,FOLLOW_17); 

                            					newLeafNode(otherlv_7, grammarAccess.getRelationAccess().getLessThanSignKeyword_0_6_1());
                            				

                            }
                            break;
                        case 3 :
                            // InternalLIRAs.g:2179:5: otherlv_8= '>='
                            {
                            otherlv_8=(Token)match(input,53,FOLLOW_17); 

                            					newLeafNode(otherlv_8, grammarAccess.getRelationAccess().getGreaterThanSignEqualsSignKeyword_0_6_2());
                            				

                            }
                            break;
                        case 4 :
                            // InternalLIRAs.g:2184:5: otherlv_9= '<='
                            {
                            otherlv_9=(Token)match(input,54,FOLLOW_17); 

                            					newLeafNode(otherlv_9, grammarAccess.getRelationAccess().getLessThanSignEqualsSignKeyword_0_6_3());
                            				

                            }
                            break;
                        case 5 :
                            // InternalLIRAs.g:2189:5: otherlv_10= '=='
                            {
                            otherlv_10=(Token)match(input,55,FOLLOW_17); 

                            					newLeafNode(otherlv_10, grammarAccess.getRelationAccess().getEqualsSignEqualsSignKeyword_0_6_4());
                            				

                            }
                            break;
                        case 6 :
                            // InternalLIRAs.g:2194:5: otherlv_11= '!='
                            {
                            otherlv_11=(Token)match(input,56,FOLLOW_17); 

                            					newLeafNode(otherlv_11, grammarAccess.getRelationAccess().getExclamationMarkEqualsSignKeyword_0_6_5());
                            				

                            }
                            break;

                    }

                    // InternalLIRAs.g:2199:4: ( (lv_th_12_0= RULE_INT ) )
                    // InternalLIRAs.g:2200:5: (lv_th_12_0= RULE_INT )
                    {
                    // InternalLIRAs.g:2200:5: (lv_th_12_0= RULE_INT )
                    // InternalLIRAs.g:2201:6: lv_th_12_0= RULE_INT
                    {
                    lv_th_12_0=(Token)match(input,RULE_INT,FOLLOW_2); 

                    						newLeafNode(lv_th_12_0, grammarAccess.getRelationAccess().getThINTTerminalRuleCall_0_7_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRelationRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"th",
                    							lv_th_12_0,
                    							"org.eclipse.xtext.common.Terminals.INT");
                    					

                    }


                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalLIRAs.g:2219:3: (otherlv_13= 'soc' otherlv_14= '(' ( (lv_agent_15_0= ruleAgent ) ) otherlv_16= ')' (otherlv_17= '>' | otherlv_18= '<' | otherlv_19= '>=' | otherlv_20= '<=' | otherlv_21= '==' | otherlv_22= '!=' ) ( (lv_th_23_0= RULE_INT ) ) )
                    {
                    // InternalLIRAs.g:2219:3: (otherlv_13= 'soc' otherlv_14= '(' ( (lv_agent_15_0= ruleAgent ) ) otherlv_16= ')' (otherlv_17= '>' | otherlv_18= '<' | otherlv_19= '>=' | otherlv_20= '<=' | otherlv_21= '==' | otherlv_22= '!=' ) ( (lv_th_23_0= RULE_INT ) ) )
                    // InternalLIRAs.g:2220:4: otherlv_13= 'soc' otherlv_14= '(' ( (lv_agent_15_0= ruleAgent ) ) otherlv_16= ')' (otherlv_17= '>' | otherlv_18= '<' | otherlv_19= '>=' | otherlv_20= '<=' | otherlv_21= '==' | otherlv_22= '!=' ) ( (lv_th_23_0= RULE_INT ) )
                    {
                    otherlv_13=(Token)match(input,57,FOLLOW_34); 

                    				newLeafNode(otherlv_13, grammarAccess.getRelationAccess().getSocKeyword_1_0());
                    			
                    otherlv_14=(Token)match(input,37,FOLLOW_9); 

                    				newLeafNode(otherlv_14, grammarAccess.getRelationAccess().getLeftParenthesisKeyword_1_1());
                    			
                    // InternalLIRAs.g:2228:4: ( (lv_agent_15_0= ruleAgent ) )
                    // InternalLIRAs.g:2229:5: (lv_agent_15_0= ruleAgent )
                    {
                    // InternalLIRAs.g:2229:5: (lv_agent_15_0= ruleAgent )
                    // InternalLIRAs.g:2230:6: lv_agent_15_0= ruleAgent
                    {

                    						newCompositeNode(grammarAccess.getRelationAccess().getAgentAgentParserRuleCall_1_2_0());
                    					
                    pushFollow(FOLLOW_29);
                    lv_agent_15_0=ruleAgent();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getRelationRule());
                    						}
                    						add(
                    							current,
                    							"agent",
                    							lv_agent_15_0,
                    							"it.polimi.LIRAs.Agent");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    otherlv_16=(Token)match(input,38,FOLLOW_36); 

                    				newLeafNode(otherlv_16, grammarAccess.getRelationAccess().getRightParenthesisKeyword_1_3());
                    			
                    // InternalLIRAs.g:2251:4: (otherlv_17= '>' | otherlv_18= '<' | otherlv_19= '>=' | otherlv_20= '<=' | otherlv_21= '==' | otherlv_22= '!=' )
                    int alt47=6;
                    switch ( input.LA(1) ) {
                    case 51:
                        {
                        alt47=1;
                        }
                        break;
                    case 52:
                        {
                        alt47=2;
                        }
                        break;
                    case 53:
                        {
                        alt47=3;
                        }
                        break;
                    case 54:
                        {
                        alt47=4;
                        }
                        break;
                    case 55:
                        {
                        alt47=5;
                        }
                        break;
                    case 56:
                        {
                        alt47=6;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 47, 0, input);

                        throw nvae;
                    }

                    switch (alt47) {
                        case 1 :
                            // InternalLIRAs.g:2252:5: otherlv_17= '>'
                            {
                            otherlv_17=(Token)match(input,51,FOLLOW_17); 

                            					newLeafNode(otherlv_17, grammarAccess.getRelationAccess().getGreaterThanSignKeyword_1_4_0());
                            				

                            }
                            break;
                        case 2 :
                            // InternalLIRAs.g:2257:5: otherlv_18= '<'
                            {
                            otherlv_18=(Token)match(input,52,FOLLOW_17); 

                            					newLeafNode(otherlv_18, grammarAccess.getRelationAccess().getLessThanSignKeyword_1_4_1());
                            				

                            }
                            break;
                        case 3 :
                            // InternalLIRAs.g:2262:5: otherlv_19= '>='
                            {
                            otherlv_19=(Token)match(input,53,FOLLOW_17); 

                            					newLeafNode(otherlv_19, grammarAccess.getRelationAccess().getGreaterThanSignEqualsSignKeyword_1_4_2());
                            				

                            }
                            break;
                        case 4 :
                            // InternalLIRAs.g:2267:5: otherlv_20= '<='
                            {
                            otherlv_20=(Token)match(input,54,FOLLOW_17); 

                            					newLeafNode(otherlv_20, grammarAccess.getRelationAccess().getLessThanSignEqualsSignKeyword_1_4_3());
                            				

                            }
                            break;
                        case 5 :
                            // InternalLIRAs.g:2272:5: otherlv_21= '=='
                            {
                            otherlv_21=(Token)match(input,55,FOLLOW_17); 

                            					newLeafNode(otherlv_21, grammarAccess.getRelationAccess().getEqualsSignEqualsSignKeyword_1_4_4());
                            				

                            }
                            break;
                        case 6 :
                            // InternalLIRAs.g:2277:5: otherlv_22= '!='
                            {
                            otherlv_22=(Token)match(input,56,FOLLOW_17); 

                            					newLeafNode(otherlv_22, grammarAccess.getRelationAccess().getExclamationMarkEqualsSignKeyword_1_4_5());
                            				

                            }
                            break;

                    }

                    // InternalLIRAs.g:2282:4: ( (lv_th_23_0= RULE_INT ) )
                    // InternalLIRAs.g:2283:5: (lv_th_23_0= RULE_INT )
                    {
                    // InternalLIRAs.g:2283:5: (lv_th_23_0= RULE_INT )
                    // InternalLIRAs.g:2284:6: lv_th_23_0= RULE_INT
                    {
                    lv_th_23_0=(Token)match(input,RULE_INT,FOLLOW_2); 

                    						newLeafNode(lv_th_23_0, grammarAccess.getRelationAccess().getThINTTerminalRuleCall_1_5_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRelationRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"th",
                    							lv_th_23_0,
                    							"org.eclipse.xtext.common.Terminals.INT");
                    					

                    }


                    }


                    }


                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleRelation"


    // $ANTLR start "entryRuleTarget"
    // InternalLIRAs.g:2305:1: entryRuleTarget returns [EObject current=null] : iv_ruleTarget= ruleTarget EOF ;
    public final EObject entryRuleTarget() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTarget = null;


        try {
            // InternalLIRAs.g:2305:47: (iv_ruleTarget= ruleTarget EOF )
            // InternalLIRAs.g:2306:2: iv_ruleTarget= ruleTarget EOF
            {
             newCompositeNode(grammarAccess.getTargetRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleTarget=ruleTarget();

            state._fsp--;

             current =iv_ruleTarget; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleTarget"


    // $ANTLR start "ruleTarget"
    // InternalLIRAs.g:2312:1: ruleTarget returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleTarget() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalLIRAs.g:2318:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalLIRAs.g:2319:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalLIRAs.g:2319:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalLIRAs.g:2320:3: (lv_name_0_0= RULE_ID )
            {
            // InternalLIRAs.g:2320:3: (lv_name_0_0= RULE_ID )
            // InternalLIRAs.g:2321:4: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            				newLeafNode(lv_name_0_0, grammarAccess.getTargetAccess().getNameIDTerminalRuleCall_0());
            			

            				if (current==null) {
            					current = createModelElement(grammarAccess.getTargetRule());
            				}
            				setWithLastConsumed(
            					current,
            					"name",
            					lv_name_0_0,
            					"org.eclipse.xtext.common.Terminals.ID");
            			

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleTarget"


    // $ANTLR start "entryRuleAgent"
    // InternalLIRAs.g:2340:1: entryRuleAgent returns [EObject current=null] : iv_ruleAgent= ruleAgent EOF ;
    public final EObject entryRuleAgent() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAgent = null;


        try {
            // InternalLIRAs.g:2340:46: (iv_ruleAgent= ruleAgent EOF )
            // InternalLIRAs.g:2341:2: iv_ruleAgent= ruleAgent EOF
            {
             newCompositeNode(grammarAccess.getAgentRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAgent=ruleAgent();

            state._fsp--;

             current =iv_ruleAgent; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAgent"


    // $ANTLR start "ruleAgent"
    // InternalLIRAs.g:2347:1: ruleAgent returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleAgent() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalLIRAs.g:2353:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalLIRAs.g:2354:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalLIRAs.g:2354:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalLIRAs.g:2355:3: (lv_name_0_0= RULE_ID )
            {
            // InternalLIRAs.g:2355:3: (lv_name_0_0= RULE_ID )
            // InternalLIRAs.g:2356:4: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            				newLeafNode(lv_name_0_0, grammarAccess.getAgentAccess().getNameIDTerminalRuleCall_0());
            			

            				if (current==null) {
            					current = createModelElement(grammarAccess.getAgentRule());
            				}
            				setWithLastConsumed(
            					current,
            					"name",
            					lv_name_0_0,
            					"org.eclipse.xtext.common.Terminals.ID");
            			

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAgent"


    // $ANTLR start "entryRuleHuman"
    // InternalLIRAs.g:2375:1: entryRuleHuman returns [EObject current=null] : iv_ruleHuman= ruleHuman EOF ;
    public final EObject entryRuleHuman() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleHuman = null;


        try {
            // InternalLIRAs.g:2375:46: (iv_ruleHuman= ruleHuman EOF )
            // InternalLIRAs.g:2376:2: iv_ruleHuman= ruleHuman EOF
            {
             newCompositeNode(grammarAccess.getHumanRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleHuman=ruleHuman();

            state._fsp--;

             current =iv_ruleHuman; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleHuman"


    // $ANTLR start "ruleHuman"
    // InternalLIRAs.g:2382:1: ruleHuman returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleHuman() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalLIRAs.g:2388:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalLIRAs.g:2389:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalLIRAs.g:2389:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalLIRAs.g:2390:3: (lv_name_0_0= RULE_ID )
            {
            // InternalLIRAs.g:2390:3: (lv_name_0_0= RULE_ID )
            // InternalLIRAs.g:2391:4: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            				newLeafNode(lv_name_0_0, grammarAccess.getHumanAccess().getNameIDTerminalRuleCall_0());
            			

            				if (current==null) {
            					current = createModelElement(grammarAccess.getHumanRule());
            				}
            				setWithLastConsumed(
            					current,
            					"name",
            					lv_name_0_0,
            					"org.eclipse.xtext.common.Terminals.ID");
            			

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleHuman"


    // $ANTLR start "entryRuleRobot"
    // InternalLIRAs.g:2410:1: entryRuleRobot returns [EObject current=null] : iv_ruleRobot= ruleRobot EOF ;
    public final EObject entryRuleRobot() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRobot = null;


        try {
            // InternalLIRAs.g:2410:46: (iv_ruleRobot= ruleRobot EOF )
            // InternalLIRAs.g:2411:2: iv_ruleRobot= ruleRobot EOF
            {
             newCompositeNode(grammarAccess.getRobotRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleRobot=ruleRobot();

            state._fsp--;

             current =iv_ruleRobot; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleRobot"


    // $ANTLR start "ruleRobot"
    // InternalLIRAs.g:2417:1: ruleRobot returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleRobot() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalLIRAs.g:2423:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalLIRAs.g:2424:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalLIRAs.g:2424:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalLIRAs.g:2425:3: (lv_name_0_0= RULE_ID )
            {
            // InternalLIRAs.g:2425:3: (lv_name_0_0= RULE_ID )
            // InternalLIRAs.g:2426:4: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            				newLeafNode(lv_name_0_0, grammarAccess.getRobotAccess().getNameIDTerminalRuleCall_0());
            			

            				if (current==null) {
            					current = createModelElement(grammarAccess.getRobotRule());
            				}
            				setWithLastConsumed(
            					current,
            					"name",
            					lv_name_0_0,
            					"org.eclipse.xtext.common.Terminals.ID");
            			

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleRobot"


    // $ANTLR start "entryRuleLocation"
    // InternalLIRAs.g:2445:1: entryRuleLocation returns [EObject current=null] : iv_ruleLocation= ruleLocation EOF ;
    public final EObject entryRuleLocation() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleLocation = null;


        try {
            // InternalLIRAs.g:2445:49: (iv_ruleLocation= ruleLocation EOF )
            // InternalLIRAs.g:2446:2: iv_ruleLocation= ruleLocation EOF
            {
             newCompositeNode(grammarAccess.getLocationRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleLocation=ruleLocation();

            state._fsp--;

             current =iv_ruleLocation; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleLocation"


    // $ANTLR start "ruleLocation"
    // InternalLIRAs.g:2452:1: ruleLocation returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleLocation() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalLIRAs.g:2458:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalLIRAs.g:2459:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalLIRAs.g:2459:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalLIRAs.g:2460:3: (lv_name_0_0= RULE_ID )
            {
            // InternalLIRAs.g:2460:3: (lv_name_0_0= RULE_ID )
            // InternalLIRAs.g:2461:4: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            				newLeafNode(lv_name_0_0, grammarAccess.getLocationAccess().getNameIDTerminalRuleCall_0());
            			

            				if (current==null) {
            					current = createModelElement(grammarAccess.getLocationRule());
            				}
            				setWithLastConsumed(
            					current,
            					"name",
            					lv_name_0_0,
            					"org.eclipse.xtext.common.Terminals.ID");
            			

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleLocation"


    // $ANTLR start "entryRuleResource"
    // InternalLIRAs.g:2480:1: entryRuleResource returns [EObject current=null] : iv_ruleResource= ruleResource EOF ;
    public final EObject entryRuleResource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleResource = null;


        try {
            // InternalLIRAs.g:2480:49: (iv_ruleResource= ruleResource EOF )
            // InternalLIRAs.g:2481:2: iv_ruleResource= ruleResource EOF
            {
             newCompositeNode(grammarAccess.getResourceRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleResource=ruleResource();

            state._fsp--;

             current =iv_ruleResource; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleResource"


    // $ANTLR start "ruleResource"
    // InternalLIRAs.g:2487:1: ruleResource returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleResource() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalLIRAs.g:2493:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalLIRAs.g:2494:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalLIRAs.g:2494:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalLIRAs.g:2495:3: (lv_name_0_0= RULE_ID )
            {
            // InternalLIRAs.g:2495:3: (lv_name_0_0= RULE_ID )
            // InternalLIRAs.g:2496:4: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            				newLeafNode(lv_name_0_0, grammarAccess.getResourceAccess().getNameIDTerminalRuleCall_0());
            			

            				if (current==null) {
            					current = createModelElement(grammarAccess.getResourceRule());
            				}
            				setWithLastConsumed(
            					current,
            					"name",
            					lv_name_0_0,
            					"org.eclipse.xtext.common.Terminals.ID");
            			

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleResource"

    // Delegated rules


    protected DFA32 dfa32 = new DFA32(this);
    static final String dfa_1s = "\174\uffff";
    static final String dfa_2s = "\40\uffff\1\67\1\uffff\5\67\22\uffff\3\67\55\uffff\1\67\22\uffff";
    static final String dfa_3s = "\1\31\1\uffff\1\53\1\uffff\1\54\1\34\7\45\2\53\1\5\1\6\6\5\1\54\1\34\7\45\1\4\1\5\5\4\2\5\1\46\2\16\2\46\1\16\1\46\1\6\6\5\2\uffff\3\4\1\34\2\5\2\34\1\5\1\63\1\46\2\16\2\46\1\16\1\46\1\5\3\46\6\6\1\34\2\5\2\34\1\5\1\63\1\46\2\34\1\63\1\34\3\46\6\6\1\4\6\6\2\34\1\63\2\34\6\6\1\34";
    static final String dfa_4s = "\1\35\1\uffff\1\71\1\uffff\1\71\1\52\7\45\2\71\1\47\1\6\6\5\1\71\1\52\7\45\1\33\1\5\5\43\2\5\1\46\2\16\2\46\1\16\1\46\1\6\6\5\2\uffff\1\33\1\45\1\33\1\52\2\5\2\52\1\5\1\70\1\46\2\16\2\46\1\16\1\46\1\5\3\46\6\6\1\52\2\5\2\52\1\5\1\70\1\46\2\52\1\70\1\52\3\46\6\6\1\33\6\6\2\52\1\70\2\52\6\6\1\52";
    static final String dfa_5s = "\1\uffff\1\1\1\uffff\1\4\63\uffff\1\2\1\3\103\uffff";
    static final String dfa_6s = "\174\uffff}>";
    static final String[] dfa_7s = {
            "\1\2\1\uffff\1\1\1\uffff\1\3",
            "",
            "\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\6\uffff\1\14",
            "",
            "\1\5\1\6\1\7\1\10\1\11\1\12\1\13\6\uffff\1\14",
            "\1\17\14\uffff\1\15\1\16",
            "\1\20",
            "\1\21",
            "\1\22",
            "\1\23",
            "\1\24",
            "\1\25",
            "\1\26",
            "\1\27\1\30\1\31\1\32\1\33\1\34\1\35\1\36\6\uffff\1\37",
            "\1\27\1\30\1\31\1\32\1\33\1\34\1\35\1\36\6\uffff\1\37",
            "\1\46\30\uffff\1\40\1\41\1\42\1\43\1\44\1\45\1\47\2\uffff\1\50",
            "\1\51",
            "\1\52",
            "\1\53",
            "\1\54",
            "\1\55",
            "\1\56",
            "\1\57",
            "\1\30\1\31\1\32\1\33\1\34\1\35\1\36\6\uffff\1\37",
            "\1\17\14\uffff\1\15\1\16",
            "\1\60",
            "\1\61",
            "\1\62",
            "\1\63",
            "\1\64",
            "\1\65",
            "\1\66",
            "\1\67\26\uffff\1\70",
            "\1\71",
            "\1\67\1\46\25\uffff\1\70\4\uffff\1\42\1\43\1\44\1\45",
            "\1\67\1\46\25\uffff\1\70\4\uffff\1\42\1\43\1\44\1\45",
            "\1\67\1\46\25\uffff\1\70\4\uffff\1\42\1\43\1\44\1\45",
            "\1\67\1\46\25\uffff\1\70\4\uffff\1\42\1\43\1\44\1\45",
            "\1\67\1\46\25\uffff\1\70\4\uffff\1\42\1\43\1\44\1\45",
            "\1\72",
            "\1\73",
            "\1\74",
            "\1\75",
            "\1\76",
            "\1\77",
            "\1\100",
            "\1\101",
            "\1\102",
            "\1\103",
            "\1\104",
            "\1\105",
            "\1\106",
            "\1\107",
            "\1\110",
            "\1\111",
            "",
            "",
            "\1\67\26\uffff\1\70",
            "\1\67\26\uffff\1\70\11\uffff\1\112",
            "\1\67\26\uffff\1\70",
            "\1\17\14\uffff\1\15\1\16",
            "\1\113",
            "\1\114",
            "\1\17\14\uffff\1\15\1\16",
            "\1\17\14\uffff\1\15\1\16",
            "\1\115",
            "\1\116\1\117\1\120\1\121\1\122\1\123",
            "\1\124",
            "\1\125",
            "\1\126",
            "\1\127",
            "\1\130",
            "\1\131",
            "\1\132",
            "\1\133",
            "\1\134",
            "\1\135",
            "\1\136",
            "\1\137",
            "\1\137",
            "\1\137",
            "\1\137",
            "\1\137",
            "\1\137",
            "\1\17\14\uffff\1\15\1\16",
            "\1\140",
            "\1\141",
            "\1\17\14\uffff\1\15\1\16",
            "\1\17\14\uffff\1\15\1\16",
            "\1\142",
            "\1\143\1\144\1\145\1\146\1\147\1\150",
            "\1\151",
            "\1\17\14\uffff\1\15\1\16",
            "\1\17\14\uffff\1\15\1\16",
            "\1\152\1\153\1\154\1\155\1\156\1\157",
            "\1\17\14\uffff\1\15\1\16",
            "\1\160",
            "\1\161",
            "\1\162",
            "\1\163",
            "\1\163",
            "\1\163",
            "\1\163",
            "\1\163",
            "\1\163",
            "\1\67\26\uffff\1\70",
            "\1\164",
            "\1\164",
            "\1\164",
            "\1\164",
            "\1\164",
            "\1\164",
            "\1\17\14\uffff\1\15\1\16",
            "\1\17\14\uffff\1\15\1\16",
            "\1\165\1\166\1\167\1\170\1\171\1\172",
            "\1\17\14\uffff\1\15\1\16",
            "\1\17\14\uffff\1\15\1\16",
            "\1\173",
            "\1\173",
            "\1\173",
            "\1\173",
            "\1\173",
            "\1\173",
            "\1\17\14\uffff\1\15\1\16"
    };

    static final short[] dfa_1 = DFA.unpackEncodedString(dfa_1s);
    static final short[] dfa_2 = DFA.unpackEncodedString(dfa_2s);
    static final char[] dfa_3 = DFA.unpackEncodedStringToUnsignedChars(dfa_3s);
    static final char[] dfa_4 = DFA.unpackEncodedStringToUnsignedChars(dfa_4s);
    static final short[] dfa_5 = DFA.unpackEncodedString(dfa_5s);
    static final short[] dfa_6 = DFA.unpackEncodedString(dfa_6s);
    static final short[][] dfa_7 = unpackEncodedStringArray(dfa_7s);

    class DFA32 extends DFA {

        public DFA32(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 32;
            this.eot = dfa_1;
            this.eof = dfa_2;
            this.min = dfa_3;
            this.max = dfa_4;
            this.accept = dfa_5;
            this.special = dfa_6;
            this.transition = dfa_7;
        }
        public String getDescription() {
            return "822:2: ( ( () ( (lv_cs1_1_0= 'until' ) ) ( (lv_condition_2_0= ruleCondition ) ) ) | ( ( (lv_cs2_3_0= 'if' ) ) ( (lv_condition_4_0= ruleCondition ) ) otherlv_5= 'else' ( (lv_action_6_0= ruleAction ) ) ) | ( ( (lv_cs3_7_0= 'if' ) ) ( (lv_condition_8_0= ruleCondition ) ) otherlv_9= 'else' ( (lv_action_10_0= ruleAction ) ) otherlv_11= 'until' ( (lv_condition_12_0= ruleCondition ) ) ) | ( ( (lv_cs4_13_0= 'wait' ) ) ( (lv_n_14_0= RULE_INT ) ) ) )";
        }
    }
 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000200022L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000031012L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x000000000003B012L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000004010L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000000000039012L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x00000000000C0000L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0000009FC0000020L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0000000000000052L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000000000180000L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000019FC4800030L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0000019FC4800032L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x0207F80000000000L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000000000400010L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000000003000000L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0000000F00000022L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0000002000000002L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x000000002A000002L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x0000010F2A000022L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x000000202A000002L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000060000000002L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x01F8000000000000L});

}
