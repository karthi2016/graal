/*
 * Copyright (C) Inria Sophia Antipolis - Méditerranée / LIRMM
 * (Université de Montpellier & CNRS) (2014 - 2017)
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
package fr.lirmm.graphik.graal.forward_chaining.rule_applier;

import java.util.Collection;

import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.core.ConjunctiveQueryWithNegatedPart;
import fr.lirmm.graphik.graal.api.core.Rule;
import fr.lirmm.graphik.graal.api.core.Substitution;
import fr.lirmm.graphik.graal.api.core.Variable;
import fr.lirmm.graphik.graal.api.forward_chaining.RuleApplicationException;
import fr.lirmm.graphik.graal.api.forward_chaining.RuleApplier;
import fr.lirmm.graphik.graal.api.homomorphism.HomomorphismException;
import fr.lirmm.graphik.graal.core.RuleWrapper2ConjunctiveQueryWithNegation;
import fr.lirmm.graphik.graal.homomorphism.StaticHomomorphism;
import fr.lirmm.graphik.util.stream.CloseableIterator;
import fr.lirmm.graphik.util.stream.CloseableIteratorWithoutException;
import fr.lirmm.graphik.util.stream.IteratorException;

/**
 * @author Clément Sipieter (INRIA) {@literal <clement@6pi.fr>}
 *
 */
public class RestrictedChaseRuleApplier implements RuleApplier<Rule, AtomSet> {

	private static final RestrictedChaseRuleApplier INSTANCE = new RestrictedChaseRuleApplier();

	// /////////////////////////////////////////////////////////////////////////
	// SINGLETON
	// /////////////////////////////////////////////////////////////////////////

	public static RestrictedChaseRuleApplier instance() {
		return INSTANCE;
	}

	private RestrictedChaseRuleApplier() {
	}

	// /////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	// /////////////////////////////////////////////////////////////////////////

	@Override
	public boolean apply(Rule rule, AtomSet atomSet) throws RuleApplicationException {
		try {
			boolean res = false;
			ConjunctiveQueryWithNegatedPart query = new RuleWrapper2ConjunctiveQueryWithNegation(rule);
			CloseableIterator<Substitution> results;
			
			results = StaticHomomorphism.instance().execute(query, atomSet);
			while (results.hasNext()) {
				res = true;
				Substitution proj = results.next();
	
				// replace variables by fresh symbol
				for (Variable t : rule.getExistentials()) {
					proj.put(t, atomSet.getFreshSymbolGenerator().getFreshCst());
				}
	
				CloseableIteratorWithoutException<Atom> it = proj.createImageOf(rule.getHead()).iterator();
				while (it.hasNext()) {
					atomSet.add(it.next());
				}
			}
			
			return res;
		} catch (HomomorphismException e) {
			throw new RuleApplicationException("", e);
		} catch (AtomSetException e) {
			throw new RuleApplicationException("", e);
		} catch (IteratorException e) {
			throw new RuleApplicationException("", e);
		}
	}
	
	@Override
	public boolean apply(Rule rule, AtomSet atomSet, Collection<Atom> newAtomsDest) throws RuleApplicationException {
		try {
			boolean res = false;
			ConjunctiveQueryWithNegatedPart query = new RuleWrapper2ConjunctiveQueryWithNegation(rule);
			CloseableIterator<Substitution> results;
			
			results = StaticHomomorphism.instance().execute(query, atomSet);
			while (results.hasNext()) {
				res = true;
				Substitution proj = results.next();
	
				// replace variables by fresh symbol
				for (Variable t : rule.getExistentials()) {
					proj.put(t, atomSet.getFreshSymbolGenerator().getFreshCst());
				}
	
				CloseableIteratorWithoutException<Atom> it = proj.createImageOf(rule.getHead()).iterator();
				while (it.hasNext()) {
					newAtomsDest.add(it.next());
				}
			}
			return res;
		} catch (HomomorphismException e) {
			throw new RuleApplicationException("", e);
		} catch (IteratorException e) {
			throw new RuleApplicationException("", e);
		}
	}

	
	// /////////////////////////////////////////////////////////////////////////
	// OBJECT OVERRIDE METHODS
	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	// /////////////////////////////////////////////////////////////////////////

}
