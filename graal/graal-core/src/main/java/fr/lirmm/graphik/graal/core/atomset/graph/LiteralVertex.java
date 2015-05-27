/**
 * 
 */
package fr.lirmm.graphik.graal.core.atomset.graph;

import fr.lirmm.graphik.graal.core.term.Literal;
import fr.lirmm.graphik.util.URI;

/**
 * @author Clément Sipieter (INRIA) {@literal <clement@6pi.fr>}
 *
 */
final class LiteralVertex extends AbstractTermVertex implements Literal {

	private static final long serialVersionUID = 1179445673470099179L;

	private Literal term;

	// /////////////////////////////////////////////////////////////////////////
	// CONSTRUCTORS
	// /////////////////////////////////////////////////////////////////////////

	public LiteralVertex(Literal term) {
		this.term = term;
	}

	// /////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	// /////////////////////////////////////////////////////////////////////////

	@Override
	public Object getValue() {
		return this.term.getValue();
	}

	@Override
	public URI getDatatype() {
		return this.term.getDatatype();
	}

	@Override
	public String getIdentifier() {
		return this.term.getIdentifier();
	}

	@Override
	public String getLabel() {
		return this.term.getLabel();
	}

	// /////////////////////////////////////////////////////////////////////////
	// OBJECT OVERRIDE METHODS
	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	// /////////////////////////////////////////////////////////////////////////

	@Override
	protected Literal getTerm() {
		return this.term;
	}
}
