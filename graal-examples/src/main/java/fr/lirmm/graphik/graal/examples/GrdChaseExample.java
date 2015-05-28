package fr.lirmm.graphik.graal.examples;
import java.io.File;
import java.io.IOException;

import fr.lirmm.graphik.graal.core.atomset.InMemoryAtomSet;
import fr.lirmm.graphik.graal.core.atomset.graph.MemoryGraphAtomSet;
import fr.lirmm.graphik.graal.forward_chaining.Chase;
import fr.lirmm.graphik.graal.forward_chaining.ChaseException;
import fr.lirmm.graphik.graal.forward_chaining.ChaseWithGRDAndUnfiers;
import fr.lirmm.graphik.graal.grd.GraphOfRuleDependencies;
import fr.lirmm.graphik.graal.io.ParseException;
import fr.lirmm.graphik.graal.io.dlp.DlgpParser;
import fr.lirmm.graphik.graal.io.dlp.DlgpWriter;
import fr.lirmm.graphik.graal.io.grd.GRDParser;

/**
 * @author Clément Sipieter (INRIA) <clement@6pi.fr>
 *
 */
public class GrdChaseExample {
	public static void main(String[] args) throws IOException, ChaseException, ParseException   {
		
		GraphOfRuleDependencies grd = GRDParser.getInstance().parse(
				new File("./src/main/resources/test-grd.grd"));
		
		InMemoryAtomSet facts = new MemoryGraphAtomSet();
		facts.add(DlgpParser.parseAtom("r(a)."));

		Chase chase = new ChaseWithGRDAndUnfiers(grd, facts);
		chase.execute();
		
		System.out.println("########### SATURATED FACTS BASE ##############");
		DlgpWriter writer = new DlgpWriter();
		writer.write(facts);
		writer.close();
		System.out.println("###############################################");
		
	}
}
