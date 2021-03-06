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
 /**
 * 
 */
package fr.lirmm.graphik.graal.api.core;


/**
 * @author Clément Sipieter (INRIA) {@literal <clement@6pi.fr>}
 *
 */
public abstract class AbstractTerm implements Term {

	private static final long serialVersionUID = 5255497469444828872L;

	// /////////////////////////////////////////////////////////////////////////
	// CONSTRUCTORS
	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	// /////////////////////////////////////////////////////////////////////////

	@Override
	public String getLabel() {
		return this.getIdentifier().toString();
	}

	@Override
	public final boolean isConstant() {
		return this instanceof Constant;
	}

	@Override
	public final boolean isVariable() {
		return this instanceof Variable;
	}
	
	@Override
	public final boolean isLiteral() {
		return this instanceof Literal;
	}

	// /////////////////////////////////////////////////////////////////////////
	// OBJECT OVERRIDE METHODS
	// /////////////////////////////////////////////////////////////////////////

	@Override
	public int compareTo(Term o) {
		if(this.getIdentifier().equals(o.getIdentifier())) {
			return 0;
		} else {
			if(this.getIdentifier().getClass().equals(o.getIdentifier().getClass())) {
				return this.getIdentifier().toString().compareTo(o.getIdentifier().toString());
			} else {
				return this.getIdentifier().getClass().toString().compareTo(o.getIdentifier().getClass().toString());
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Term)) {
			return false;
		}
		Term other = (Term) obj;
		return this.equals(other);
	}

	public boolean equals(Term term) {
		return this.getIdentifier().equals(term.getIdentifier());
	}

	@Override
	public int hashCode() {
		return this.getIdentifier().hashCode();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(this.isVariable()) {
			sb.append("VAR_");
		} else if(this.isLiteral()) {
			sb.append('"');
		} else {
			sb.append("cst_");
		}
		sb.append(this.getLabel());
		if(this.isLiteral()) {
			sb.append('"');
		}
		return sb.toString();
	}

	// /////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	// /////////////////////////////////////////////////////////////////////////

}
