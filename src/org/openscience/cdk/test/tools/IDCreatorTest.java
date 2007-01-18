/* $RCSfile$    
 * $Author$    
 * $Date$    
 * $Revision$
 * 
 * Copyright (C) 1997-2007  The Chemistry Development Kit (CDK) project
 * 
 * Contact: cdk-devel@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA. 
 */
package org.openscience.cdk.test.tools;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.openscience.cdk.Atom;
import org.openscience.cdk.Bond;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.test.CDKTestCase;
import org.openscience.cdk.tools.IDCreator;

/**
 * @cdk.module test-standard
 */
public class IDCreatorTest extends CDKTestCase {
	
	private IDCreator idCreator;
	
	public IDCreatorTest(String name) {
		super(name);
	}

	public void setUp() {
		idCreator = new IDCreator();
	};

	public static Test suite() {
		return new TestSuite(IDCreatorTest.class);
	}

	public void testCreateIDs_IAtomContainer() {
		Molecule mol = new Molecule();
        Atom atom1 = new Atom("C");
        Atom atom2 = new Atom("C");
        mol.addAtom(atom1);
        mol.addAtom(atom2);
        Bond bond = new Bond(atom1, atom2);
        mol.addBond(bond);
        
        idCreator.createIDs(mol);
        
        assertEquals("a1", atom1.getID());
        assertEquals("b1", bond.getID());
	}
	
	public void testKeepingIDs() {
		Molecule mol = new Molecule();
        Atom atom = new Atom("C");
        atom.setID("atom1");
        mol.addAtom(atom);
        
        idCreator.createIDs(mol);
        
        assertEquals("atom1", atom.getID());
        assertNotNull(mol.getID());
	}
	
	public void testNoDuplicateCreation() {
		Molecule mol = new Molecule();
        Atom atom1 = new Atom("C");
        Atom atom2 = new Atom("C");
        atom1.setID("a1");
        mol.addAtom(atom2);
        mol.addAtom(atom1);
        
        idCreator.createIDs(mol);
        
        assertEquals("a2", atom2.getID());
	}
	
}

