/* Graal v0.7.4
 * Copyright (c) 2014-2015 Inria Sophia Antipolis - Méditerranée / LIRMM (Université de Montpellier & CNRS)
 * All rights reserved.
 * This file is part of Graal <https://graphik-team.github.io/graal/>.
 *
 * Author(s): Clément SIPIETER
 *            Mélanie KÖNIG
 *            Swan ROCHER
 *            Jean-François BAGET
 *            Michel LECLÈRE
 *            Marie-Laure MUGNIER
 */
 /**
 * 
 */
package fr.lirmm.graphik.graal.core.atomset.graph;

import java.util.Set;
import java.util.TreeSet;

import fr.lirmm.graphik.graal.core.term.AbstractTerm;
import fr.lirmm.graphik.graal.core.term.Term;

abstract class AbstractTermVertex extends AbstractTerm implements TermVertex {

	private static final long serialVersionUID = -1087277093687686210L;

	private final TreeSet<Edge> edges = new TreeSet<Edge>();

	// /////////////////////////////////////////////////////////////////////////
	// ABSTRACT METHODS
	// /////////////////////////////////////////////////////////////////////////

	protected abstract Term getTerm();

	// /////////////////////////////////////////////////////////////////////////
	// VERTEX METHODS
	// /////////////////////////////////////////////////////////////////////////

	@Override
	public Set<Edge> getEdges() {
		return this.edges;
	}

	// /////////////////////////////////////////////////////////////////////////
	// TERM METHODS
	// /////////////////////////////////////////////////////////////////////////

	@Override
	public boolean isConstant() {
		return this.getTerm().isConstant();
	}

	@Override
	public Type getType() {
		return this.getTerm().getType();
	}

	@Override
	public String toString() {
		return this.getTerm().toString();
	}

}