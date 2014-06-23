/**
 * 
 */
package fr.lirmm.graphik.obda.parser.semanticweb;

import java.util.Iterator;

import fr.lirmm.graphik.kb.core.Atom;
import fr.lirmm.graphik.kb.core.DefaultAtom;
import fr.lirmm.graphik.kb.core.DefaultRule;
import fr.lirmm.graphik.kb.core.Predicate;
import fr.lirmm.graphik.kb.core.Rule;
import fr.lirmm.graphik.kb.core.Term;
import fr.lirmm.graphik.kb.core.Term.Type;
import fr.lirmm.graphik.util.stream.AbstractReader;

/**
 * @author Clément Sipieter (INRIA) <clement@6pi.fr>
 * 
 */
public class RDFS2Rules extends AbstractReader<Object> {

	public static final String RDFS_PREFIX = "http://www.w3.org/2000/01/rdf-schema#";

	/** range(p,c) => c(Y) :- p(X,Y) */
	public static final String RDFS_RANGE = RDFS_PREFIX + "range";

	/** domain(p,c) => c(X) :- p(X,Y) */
	public static final String RDFS_DOMAIN = RDFS_PREFIX + "domain";
	
	
	/** subClassOf(c1, c2) => c2(X) :- c1(X) */
	public static final String RDFS_SUB_CLASS_OF = RDFS_PREFIX + "subClassOf";
	
	/** subPropertyOf(p1, p2) => p2(X,Y) :- p1(X,Y); */
	public static final String RDFS_SUB_PROPERTY_OF = RDFS_PREFIX
													  + "subPropertyOf";
	
	/** */
	public static final String RDFS_LABEL = RDFS_PREFIX + "label";
	
	/** */
	public static final String RDFS_COMMENT = RDFS_PREFIX + "comment";

	protected static final Term x = new Term("X", Type.VARIABLE);
	protected static final Term y = new Term("Y", Type.VARIABLE);
	protected static final Term z = new Term("Z", Type.VARIABLE);

	private Iterator<Atom> reader;
	
	// /////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	// /////////////////////////////////////////////////////////////////////////

	public RDFS2Rules(Iterator<Atom> atomReader) {
		this.reader = new RDF2Atom(atomReader);
	}
	// /////////////////////////////////////////////////////////////////////////
	// METHODS
	// /////////////////////////////////////////////////////////////////////////

	/* (non-Javadoc)
	 * @see fr.lirmm.graphik.util.stream.ObjectReader#next()
	 */
	@Override
	public Object next() {
		Object o = null;
		Atom a = this.reader.next();

		String predicateLabel = a.getPredicate().toString();
		if (RDFS_RANGE.equals(predicateLabel)) {
			Rule rule = new DefaultRule();
			Predicate p = new Predicate(a.getTerm(0).toString(), 2);
			rule.getBody().add(new DefaultAtom(p, x, y));
			p = new Predicate(a.getTerm(1).toString(), 1);
			rule.getHead().add(new DefaultAtom(p, y));
			o = rule;

		} else if (RDFS_DOMAIN.equals(predicateLabel)) {
			Rule rule = new DefaultRule();
			Predicate p = new Predicate(a.getTerm(0).toString(), 2);
			rule.getBody().add(new DefaultAtom(p, x, y));
			p = new Predicate(a.getTerm(1).toString(), 1);
			rule.getHead().add(new DefaultAtom(p, x));
			o = rule;
			
		} else if (RDFS_SUB_CLASS_OF.equals(predicateLabel)) {
			Rule rule = new DefaultRule();
			Predicate p1 = new Predicate(a.getTerm(0).toString(), 1);
			Predicate p2 = new Predicate(a.getTerm(1).toString(), 1);
			rule.getBody().add(new DefaultAtom(p1, x));
			rule.getHead().add(new DefaultAtom(p2, x));
			o = rule;
			
		} else if (RDFS_SUB_PROPERTY_OF.equals(predicateLabel)) {
			Rule rule = new DefaultRule();
			Predicate p1 = new Predicate(a.getTerm(0).toString(), 2);
			Predicate p2 = new Predicate(a.getTerm(1).toString(), 2);
			rule.getBody().add(new DefaultAtom(p1, x, y));
			rule.getHead().add(new DefaultAtom(p2, x, y));
			o = rule;
			
		} else {
			o = a;
		}

		return o;
	}

	/* (non-Javadoc)
	 * @see fr.lirmm.graphik.util.stream.ObjectReader#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return this.reader.hasNext();
	}

	
}
