/*
 * Copyright (C) Inria Sophia Antipolis - Méditerranée / LIRMM
 * (Université de Montpellier & CNRS) (2014 - 2015)
 *
 * Contributors :
 *
 * Clément SIPIETER <clement.sipieter@inria.fr>
 * Mélanie KÖNIG
 * Swan ROCHER
 * Jean-François BAGET
 * Michel LECLÈRE
 * Marie-Laure MUGNIER <mugnier@lirmm.fr>
 *
 *
 * This file is part of Graal <https://graphik-team.github.io/graal/>.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
 package fr.lirmm.graphik.graal.rulesetanalyser.property;

import java.util.List;
import java.util.LinkedList;


import java.util.Set;

import fr.lirmm.graphik.graal.core.Atom;
import fr.lirmm.graphik.graal.core.Rule;
import fr.lirmm.graphik.graal.core.term.Term;

/**
 * At least one atom in the body (called a guard) contains all the variables
 * from the body.
 * 
 * @author Clément Sipieter (INRIA) {@literal <clement@6pi.fr>}
 * @author Swan Rocher
 * 
 */
public final class GuardedProperty extends RuleSetProperty.Local {

	private static GuardedProperty instance = null;
	
	private GuardedProperty(){}
	
	public static synchronized GuardedProperty instance() {
		if(instance == null) {
			instance = new GuardedProperty();
		}
		return instance;	
	}
	
	@Override
	public int check(Rule rule) {
		Set<Term> bodyVars = rule.getBody().getTerms(Term.Type.VARIABLE);
		boolean isGuarded = true;

		for (Atom a : rule.getBody()) {
			isGuarded = true;
			for (Term v : bodyVars) {
				if (!a.getTerms().contains(v)) {
					isGuarded = false;
					break;
				}
			}
			if (isGuarded) {
				break;
			}
		}

		if (isGuarded) return 1;
		return -1;
	}

	@Override
	public String getLabel() {
		return "g";
	}

	@Override
	public Iterable<RuleSetProperty> getGeneralisations() {
		List<RuleSetProperty> gen = new LinkedList<RuleSetProperty>();
		gen.add(FrontierGuardedProperty.instance());
		gen.add(WeaklyGuardedSetProperty.instance());
		gen.add(WeaklyFrontierGuardedSetProperty.instance());
		gen.add(GBTSProperty.instance());
		gen.add(BTSProperty.instance());
		return gen;
	}

};
