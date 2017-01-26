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
package fr.lirmm.graphik.graal.homomorphism;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.tuple.Pair;

import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.core.ConjunctiveQuery;
import fr.lirmm.graphik.graal.api.core.RulesCompilation;
import fr.lirmm.graphik.graal.api.core.Substitution;
import fr.lirmm.graphik.graal.api.core.Term;
import fr.lirmm.graphik.graal.api.core.Variable;
import fr.lirmm.graphik.graal.api.homomorphism.HomomorphismException;
import fr.lirmm.graphik.graal.core.HashMapSubstitution;
import fr.lirmm.graphik.util.stream.CloseableIterator;
import fr.lirmm.graphik.util.stream.CloseableIteratorAdapter;
import fr.lirmm.graphik.util.stream.CloseableIteratorAggregator;
import fr.lirmm.graphik.util.stream.converter.ConversionException;
import fr.lirmm.graphik.util.stream.converter.Converter;
import fr.lirmm.graphik.util.stream.converter.ConverterCloseableIterator;

/**
 * An homomorphism for Atomic query without multiple occurences of a variables.
 * 
 * @author Clément Sipieter (INRIA) {@literal <clement@6pi.fr>}
 *
 */
public class AtomicQueryHomomorphism extends AbstractHomomorphismWithCompilation<ConjunctiveQuery, AtomSet> {

	// /////////////////////////////////////////////////////////////////////////
	// CONSTRUCTORS
	// /////////////////////////////////////////////////////////////////////////

	private static final AtomicQueryHomomorphism INSTANCE = new AtomicQueryHomomorphism();

	// /////////////////////////////////////////////////////////////////////////
	// CONSTRUCTORS
	// /////////////////////////////////////////////////////////////////////////

	public static AtomicQueryHomomorphism instance() {
		return INSTANCE;
	}

	// /////////////////////////////////////////////////////////////////////////
	// HOMOMORPHISM METHODS
	// /////////////////////////////////////////////////////////////////////////
	
	@Override
	public <U1 extends ConjunctiveQuery, U2 extends AtomSet> CloseableIterator<Substitution> execute(U1 q, U2 a) throws HomomorphismException {
		try {
			return new ConverterCloseableIterator<Atom, Substitution>(a.atomsByPredicate(q.getAtomSet().iterator().next().getPredicate()), new Atom2SubstitutionConverter(q.getAtomSet().iterator().next(), q.getAnswerVariables()));
		} catch (AtomSetException e) {
			throw new HomomorphismException(e);
		}
	}
	
	@Override
	public <U1 extends ConjunctiveQuery, U2 extends AtomSet> CloseableIterator<Substitution> execute(U1 q, U2 a, RulesCompilation rc) throws HomomorphismException {
		try {
			List<CloseableIterator<Substitution>> iteratorsList = new LinkedList<CloseableIterator<Substitution>>();
			Atom atom = q.getAtomSet().iterator().next();
			for (Pair<Atom, Substitution> im : rc.getRewritingOf(atom)) {
				iteratorsList.add(new ConverterCloseableIterator<Atom, Substitution>(a.atomsByPredicate(im.getLeft().getPredicate()), new Atom2SubstitutionConverter(q.getAtomSet().iterator().next(), q.getAnswerVariables(), im.getRight())));
			}
			return new CloseableIteratorAggregator<Substitution>(new CloseableIteratorAdapter<CloseableIterator<Substitution>>(iteratorsList.iterator()));
		} catch (AtomSetException e) {
			throw new HomomorphismException(e);
		}
	}


	// /////////////////////////////////////////////////////////////////////////
	// PRIVATE CLASS
	// /////////////////////////////////////////////////////////////////////////
	
	private class Atom2SubstitutionConverter implements Converter<Atom, Substitution> {
		
		private Map<Variable, Integer> variables = new TreeMap<Variable, Integer>();
		private List<Term> ans;
		
		public Atom2SubstitutionConverter(Atom query, List<Term> ans) {
			this.ans = ans;
			int i = 0;
			for(Term t : query) {
				if(ans.contains(t))	 {
					variables.put((Variable) t, i);
				}
				++i;
			}
		}
		
		public Atom2SubstitutionConverter(Atom query, List<Term> ans, Substitution rew) {
			this.ans = ans;
			int i = 0;
			for(Term t : query) {
				if(ans.contains(t))	 {
					variables.put((Variable) t, i);
				}
				++i;
			}
			for(Variable var : rew.getTerms()) {
				variables.put(var, variables.get(rew.createImageOf(var)));
			}
		}
		
		@Override
		public Substitution convert(Atom object) throws ConversionException {
			Substitution s = new HashMapSubstitution();
			for (Term var : ans) { 
				s.put((Variable) var, object.getTerm(variables.get(var)));
			}
			return s;
		}
	}

}