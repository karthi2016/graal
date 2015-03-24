/**
 * 
 */
package fr.lirmm.graphik.graal.apps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import fr.lirmm.graphik.graal.backward_chaining.pure.AggregAllRulesOperator;
import fr.lirmm.graphik.graal.backward_chaining.pure.AggregSingleRuleOperator;
import fr.lirmm.graphik.graal.backward_chaining.pure.BasicAggregAllRulesOperator;
import fr.lirmm.graphik.graal.backward_chaining.pure.RewritingOperator;
import fr.lirmm.graphik.graal.backward_chaining.pure.rules.RulesCompilation;
import fr.lirmm.graphik.graal.core.ConjunctiveQuery;
import fr.lirmm.graphik.graal.core.Rule;
import fr.lirmm.graphik.graal.core.ruleset.RuleSet;
import fr.lirmm.graphik.graal.io.ConjunctiveQueryWriter;
import fr.lirmm.graphik.graal.io.dlp.Dlgp1Parser;
import fr.lirmm.graphik.util.stream.FilterIterator;

/**
 * @author Clément Sipieter (INRIA) {@literal <clement@6pi.fr>}
 *
 */
@Parameters(commandDescription = "Rewrite given queries")
class RewriteCommand extends PureCommand {
	
	public static final String NAME = "rewrite";
	
	private ConjunctiveQueryWriter writer;

	@Parameter(names = { "-h", "--help" }, description = "Print this message", help = true)
	private boolean help;

	@Parameter(description = "<DLGP ontology file>", required = true)
	private List<String> ontologyFile = new LinkedList<String>();

	@Parameter(names = { "-q", "--queries" }, description = "The queries to rewrite in DLGP", required = true)
	private String query = null;

	@Parameter(names = { "-c", "--compilation-file" }, description = "The compilation file")
	private String compilationFile = "";

	@Parameter(names = { "-t", "--compilation-type" }, description = "Compilation type H, ID, NONE")
	private String compilationType = "ID";

	@Parameter(names = { "-o", "--operator" }, description = "Rewriting operator SRA, ARA, ARAM")
	private String operator = "SRA";
	
	@Parameter(names = { "-u", "--unfold" }, description = "Enable unfolding")
	private boolean isUnfoldingEnable = false;

	////////////////////////////////////////////////////////////////////////////
	// 
	////////////////////////////////////////////////////////////////////////////
	
	public RewriteCommand(ConjunctiveQueryWriter writer) {
		this.writer = writer;
	}
	
	////////////////////////////////////////////////////////////////////////////
	// 
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 * @param commander
	 * @throws FileNotFoundException
	 */
	public void run(JCommander commander) throws FileNotFoundException {
		if (this.help) {
			commander.usage(NAME);
			System.exit(0);
		}

		RuleSet rules = Util.parseOntology(this.ontologyFile.get(0));
		RulesCompilation compilation = Util.selectCompilationType(this.compilationType);
		RewritingOperator operator = selectOperator();

		compilation.setProfiler(this.getProfiler());
		operator.setProfiler(this.getProfiler());
		
		if (this.compilationFile.isEmpty()) {
			compilation.compile(rules.iterator());
		} else {
			compilation.load(rules.iterator(), new FilterIterator<Object, Rule>(new Dlgp1Parser(
					new File(this.compilationFile)), new RulesFilter()));
		}
	
		this.processQuery(rules, compilation, operator);
	}

	private RewritingOperator selectOperator() {
		RewritingOperator operator = null;	
		if("SRA".equals(this.operator)) {
			operator = new AggregSingleRuleOperator();
		} else if ("ARAM".equals(this.operator)) {
			operator = new AggregAllRulesOperator();
		} else {
			operator = new BasicAggregAllRulesOperator();
		}
		return operator;
	}
	
	private void processQuery(RuleSet rules, RulesCompilation compilation, RewritingOperator operator)
			throws FileNotFoundException {
		List<ConjunctiveQuery> queries = new LinkedList<ConjunctiveQuery>();
		File file = new File(this.query);
		if (file.exists()) {
			Iterator<ConjunctiveQuery> it = new FilterIterator<Object, ConjunctiveQuery>(
					new Dlgp1Parser(file), new ConjunctiveQueryFilter());
			while (it.hasNext()) {
				queries.add(it.next());
			}
		} else {
			queries.add(Dlgp1Parser.parseQuery(this.query));
		}

		for (ConjunctiveQuery query : queries) {
			if (this.isVerbose()) {
				this.getProfiler().clear();
				this.getProfiler().add("Initial query", query);
			}
			fr.lirmm.graphik.graal.backward_chaining.PureRewriter bc = new fr.lirmm.graphik.graal.backward_chaining.PureRewriter(
					query, rules, compilation, operator);

			if (this.isVerbose()) {
				bc.setProfiler(this.getProfiler());
				bc.enableVerbose(true);
			}
			
			bc.enableUnfolding(this.isUnfoldingEnable);

			try {
				writer.write("\n");
				writer.write("% rewrite of: ");
				writer.write(query);
				while (bc.hasNext()) {
					writer.write(bc.next());
				}
			} catch (IOException e) {
			}

		}
	}


}
