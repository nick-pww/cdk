/*
 * Copyright (c) 2013 John May <jwmay@users.sf.net>
 *
 * Contact: cdk-devel@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * All we ask is that proper credit is given for our work, which includes
 * - but is not limited to - adding the above copyright notice to the beginning
 * of your source code files, and to any copyright notice that you may distribute
 * with programs based on this work.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 U
 */

package org.openscience.cdk.hash;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.graph.AtomContainerAtomPermutor;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.io.iterator.IteratingSDFReader;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.openscience.cdk.CDKConstants.TITLE;

/**
 * This test class provides several scenario tests for the {@literal cdk-hash}
 * module.
 *
 * @author John May
 * @cdk.module test-hash
 */
public class HashCodeScenarios {

    /**
     * Two molecules with identical Racid identification numbers, these hash
     * codes should be different.
     */
    @Test public void figure2a() {

        List<IAtomContainer> mols = sdf("/data/hash/ihlenfeldt93-figure-2a.sdf", 2);

        IAtomContainer a = mols.get(0);
        IAtomContainer b = mols.get(1);

        MoleculeHashGenerator generator = new HashGeneratorMaker().elemental()
                                                                  .depth(6)
                                                                  .molecular();

        long aHash = generator.generate(a);
        long bHash = generator.generate(b);

        Assert.assertThat(nonEqMesg(a, b),
                          aHash, is(not(bHash)));
    }

    /**
     * Two molecules with identical Racid identification numbers, these hash
     * codes should be different.
     */
    @Test public void figure2b() {

        List<IAtomContainer> mols = sdf("/data/hash/ihlenfeldt93-figure-2b.sdf", 2);

        IAtomContainer a = mols.get(0);
        IAtomContainer b = mols.get(1);

        MoleculeHashGenerator generator = new HashGeneratorMaker().elemental()
                                                                  .depth(6)
                                                                  .molecular();

        long aHash = generator.generate(a);
        long bHash = generator.generate(b);

        Assert.assertThat(nonEqMesg(a, b),
                          aHash, is(not(bHash)));
    }

    /**
     * Two molecules with identical Racid identification numbers, these hash
     * codes should be different.
     */
    @Test public void figure2c() {
        List<IAtomContainer> mols = sdf("/data/hash/ihlenfeldt93-figure-2c.sdf", 2);

        IAtomContainer a = mols.get(0);
        IAtomContainer b = mols.get(1);

        MoleculeHashGenerator generator = new HashGeneratorMaker().elemental()
                                                                  .depth(6)
                                                                  .molecular();

        long aHash = generator.generate(a);
        long bHash = generator.generate(b);

        Assert.assertThat(nonEqMesg(a, b),
                          aHash, is(not(bHash)));
    }

    /**
     * These two molecules from the original publication collide when using a
     * previous hash coding method (Bawden, 81). The hash codes should be
     * different using this method.
     */
    @Test public void figure3() {

        List<IAtomContainer> mols = sdf("/data/hash/ihlenfeldt93-figure-3.sdf", 2);

        IAtomContainer a = mols.get(0);
        IAtomContainer b = mols.get(1);

        MoleculeHashGenerator generator = new HashGeneratorMaker().elemental()
                                                                  .depth(6)
                                                                  .molecular();

        long aHash = generator.generate(a);
        long bHash = generator.generate(b);

        Assert.assertThat(nonEqMesg(a, b),
                          aHash, is(not(bHash)));
    }

    /**
     * These two molecules have atoms experiencing uniform environments but
     * where the number of atoms between the molecules is different. This
     * demonstrates the size the molecule is considered when hashing.
     */
    @Test public void figure7() {

        List<IAtomContainer> mols = sdf("/data/hash/ihlenfeldt93-figure-7.sdf", 2);

        IAtomContainer a = mols.get(0);
        IAtomContainer b = mols.get(1);

        MoleculeHashGenerator generator = new HashGeneratorMaker().elemental()
                                                                  .depth(6)
                                                                  .molecular();

        long aHash = generator.generate(a);
        long bHash = generator.generate(b);

        Assert.assertThat(nonEqMesg(a, b),
                          aHash, is(not(bHash)));
    }

    /**
     * These molecules are erroneous structures from a catalogue file, the
     * German names are the original names as they appear in the catalogue. The
     * hash code identifies that the two molecules are the same.
     */
    @Test public void figure10() {
        List<IAtomContainer> mols = sdf("/data/hash/ihlenfeldt93-figure-10.sdf", 2);

        IAtomContainer a = mols.get(0);
        IAtomContainer b = mols.get(1);

        MoleculeHashGenerator generator = new HashGeneratorMaker().elemental()
                                                                  .depth(6)
                                                                  .molecular();

        long aHash = generator.generate(a);
        long bHash = generator.generate(b);

        Assert.assertThat(eqMesg(a, b),
                          aHash, is(bHash));
    }

    /**
     * This structure is an example where the Cahn-Ingold-Prelog (CIP) rules can
     * not discriminate two neighbours of chiral atom. Due to this, the CIP
     * rules are not used as an atom seed and instead a bootstrap method is
     * used. Please refer to the original article for the exact method.
     */
    @Test public void figure11() {

        List<IAtomContainer> mols = sdf("/data/hash/ihlenfeldt93-figure-11.sdf", 1);

        IAtomContainer molecule = mols.get(0);

        MoleculeHashGenerator basic = new HashGeneratorMaker().elemental()
                                                              .depth(8)
                                                              .molecular();
        MoleculeHashGenerator stereo = new HashGeneratorMaker().elemental()
                                                               .depth(8)
                                                               .chiral()
                                                               .molecular();

        long basicHash = basic.generate(molecule);
        long stereoHash = stereo.generate(molecule);

        assertThat("If the stereo-centre was perceived then the basic hash should be different from the chiral hash code",
                   basicHash, is(not(stereoHash)));
    }

    /**
     * This scenario demonstrates how stereo-chemistry encoding is invariant
     * under permutation. A simple molecule 'bromo(chloro)fluoromethane' is
     * permuted to all 120 possible atom orderings. It is checked that the (R)-
     * configuration  and (S)- configuration values are invariant
     */
    @Test public void figure12() {
        List<IAtomContainer> mols = sdf("/data/hash/ihlenfeldt93-figure-12.sdf", 2);

        MoleculeHashGenerator stereo = new HashGeneratorMaker().elemental()
                                                               .depth(1)
                                                               .chiral()
                                                               .molecular();


        Set<Long> sHashes = new HashSet<Long>();
        Set<Long> rHashes = new HashSet<Long>();

        AtomContainerAtomPermutor rpermutor = new AtomContainerAtomPermutor(mols.get(0));
        AtomContainerAtomPermutor spermutor = new AtomContainerAtomPermutor(mols.get(1));

        while (rpermutor.hasNext() && spermutor.hasNext()) {
            IAtomContainer r = rpermutor.next();
            IAtomContainer s = spermutor.next();
            sHashes.add(stereo.generate(s));
            rHashes.add(stereo.generate(r));
        }
        Assert.assertThat("all (S)-bromo(chloro)fluoromethane permutation produce a single hash code",
                          sHashes.size(), CoreMatchers.is(1));
        Assert.assertThat("all (R)-bromo(chloro)fluoromethane permutation produce a single hash code",
                          rHashes.size(), CoreMatchers.is(1));
        sHashes.addAll(rHashes);
        Assert.assertThat(sHashes.size(), CoreMatchers.is(2));
    }

    /**
     * This molecule has a tetrahedral stereo-centre depends on the configuration of two double bonds. Swapping the double bond configuration inverts the tetrahedral stereo-centre (R/S) and produces different hash codes.
     */
    @Test public void figure13a() {

        List<IAtomContainer> mols = sdf("/data/hash/ihlenfeldt93-figure-13a.sdf", 2);

        IAtomContainer a = mols.get(0);
        IAtomContainer b = mols.get(1);

        MoleculeHashGenerator stereo = new HashGeneratorMaker().elemental()
                                                               .depth(8)
                                                               .chiral()
                                                               .molecular();
        long aHash = stereo.generate(a);
        long bHash = stereo.generate(b);

        assertThat(nonEqMesg(a, b),
                   aHash, is(not(bHash)));
    }

    /**
     * This molecule has double bond stereo chemistry defined only by differences in the configurations of it's substituents. The
     * two configurations the bond can take (Z/E) and should produce different hash codes.
     */
    @Test public void figure13b() {
        List<IAtomContainer> mols = sdf("/data/hash/ihlenfeldt93-figure-13b.sdf", 2);

        IAtomContainer a = mols.get(0);
        IAtomContainer b = mols.get(1);

        MoleculeHashGenerator stereo = new HashGeneratorMaker().elemental()
                                                               .depth(8)
                                                               .chiral()
                                                               .molecular();

        assertThat(nonEqMesg(a, b),
                   stereo.generate(a), is(not(stereo.generate(b))));
    }

    /**
     * These two structures were found in the original publication as duplicates in the catalogue of the CHIRON program. The article notes the second name is likely incorrect but that this is how it appears in the catalogue. The two molecules are in fact the same and generate the same hash code.
     */
    @Test public void figure14() {

        List<IAtomContainer> mols = sdf("/data/hash/ihlenfeldt93-figure-14.sdf", 2);

        IAtomContainer a = mols.get(0);
        IAtomContainer b = mols.get(1);

        MoleculeHashGenerator generator = new HashGeneratorMaker().elemental()
                                                                  .depth(6)
                                                                  .molecular();

        long aHash = generator.generate(a);
        long bHash = generator.generate(b);

        Assert.assertThat(eqMesg(a,b),
                          aHash, is(bHash));
    }

    /**
     * These two compounds are connected differently but produce the same basic hash code. In order to discriminate them we must use the perturbed hash code.
     */
    @Test public void figure15() {

        List<IAtomContainer> mols = sdf("/data/hash/ihlenfeldt93-figure-15.sdf", 2);

        IAtomContainer a = mols.get(0);
        IAtomContainer b = mols.get(1);

        MoleculeHashGenerator generator = new HashGeneratorMaker().elemental()
                                                                  .depth(6)
                                                                  .molecular();
        long aHash = generator.generate(a);
        long bHash = generator.generate(b);

        Assert.assertThat(eqMesg(a,b),
                          aHash, is(bHash));


        MoleculeHashGenerator perturbed = new HashGeneratorMaker().elemental()
                                                                  .depth(6)
                                                                  .perturbed()
                                                                  .molecular();
        aHash = perturbed.generate(a);
        bHash = perturbed.generate(b);
        Assert.assertThat(nonEqMesg(a,b),
                          aHash, is(not(bHash)));
    }

    /**
     * The molecules cubane and cuneane have the same number of atoms all of which experience the same environment in the first sphere. Using a non-perturbed hash code, these will hash to the same value. The perturbed hash code, allows us to discriminate them.
     */
    @Test public void figure16a() {

        List<IAtomContainer> mols = sdf("/data/hash/ihlenfeldt93-figure-16a.sdf", 2);

        IAtomContainer a = mols.get(0);
        IAtomContainer b = mols.get(1);

        MoleculeHashGenerator nonperturbed = new HashGeneratorMaker()
                .elemental()
                .depth(6)
                .molecular();
        MoleculeHashGenerator perturbed = new HashGeneratorMaker().elemental()
                                                                  .depth(6)
                                                                  .perturbed()
                                                                  .molecular();

        long aHash = nonperturbed.generate(a);
        long bHash = nonperturbed.generate(b);
        Assert.assertThat(eqMesg(a,b),
                          aHash, is(bHash));

        aHash = perturbed.generate(a);
        bHash = perturbed.generate(b);
        Assert.assertThat(nonEqMesg(a,b),
                          aHash, is(not(bHash)));


        AtomHashGenerator perturbedAtomic = new HashGeneratorMaker().elemental()
                                                                    .depth(3)
                                                                    .perturbed()
                                                                    .atomic();
        long[] aHashes = perturbedAtomic.generate(a);
        long[] bHashes = perturbedAtomic.generate(b);

        assertThat("cubane has 1 equiavelnt class",
                   toSet(aHashes).size(), is(1));
        assertThat("cubane has 3 equiavelnt classes",
                    toSet(bHashes).size(), is(3));
    }

    private Set<Long> toSet(long[] xs) {
        Set<Long> set = new HashSet<Long>();
        for (long x : xs) {
            set.add(x);
        }
        return set;
    }

    /**
     * A chlorinated cubane and cuneane can not be told apart by the basic hash code. However using perturbed hash codes
     * is is possible to tell them apart as well as the 3 different chlorination locations on the cuneane
     */
    @Test public void figure16b() {

        List<IAtomContainer> mols = sdf("/data/hash/ihlenfeldt93-figure-16b.sdf", 4);

        IAtomContainer a = mols.get(0);
        IAtomContainer b = mols.get(1);
        IAtomContainer c = mols.get(2);
        IAtomContainer d = mols.get(3);

        MoleculeHashGenerator generator = new HashGeneratorMaker().elemental()
                                                                  .depth(6)
                                                                  .perturbed()
                                                                  .molecular();

        long aHash = generator.generate(a);
        long bHash = generator.generate(b);
        long cHash = generator.generate(c);
        long dHash = generator.generate(d);

        Assert.assertThat(nonEqMesg(a, b), aHash, is(not(bHash)));
        Assert.assertThat(nonEqMesg(a, c), aHash, is(not(cHash)));
        Assert.assertThat(nonEqMesg(a, d), aHash, is(not(dHash)));
        Assert.assertThat(nonEqMesg(a, c), bHash, is(not(cHash)));
        Assert.assertThat(nonEqMesg(b, d), bHash, is(not(dHash)));
        Assert.assertThat(nonEqMesg(c, d), cHash, is(not(dHash)));

    }


    /**
     * This scenario demonstrates how the depth influences the hash code. These
     * two molecules differ only by length of their aliphatic chains. One  has chains of length 10 and 11 and other of length 11 and 10 (connected the other way). To tell these apart the depth must be large enough to propagate  the environments from the ends of both chains.
     */
    @Test public void aminotetracosanone() {

        List<IAtomContainer> aminotetracosanones = sdf("/data/hash/aminotetracosanones.sdf", 2);

        IAtomContainer a = aminotetracosanones.get(0);
        IAtomContainer b = aminotetracosanones.get(1);

        for (int depth = 0; depth < 12; depth++) {
            MoleculeHashGenerator basic = new HashGeneratorMaker().elemental()
                                                                  .depth(depth)
                                                                  .molecular();
            long aHash = basic.generate(a);
            long bHash = basic.generate(b);

            if (depth < 7) {
                assertThat(eqMesg(a,b) + " at depth " + depth, aHash, is(bHash));
            } else {
                assertThat(nonEqMesg(a,b) + " at depth " + depth,
                           aHash, is(not(bHash)));
            }
        }

    }

    /**
     * This test demonstrates that the nine stereo isomers of inositol can be
     * hashed to the same value or to different values (perturbed).
     *
     * @see <a href="http://en.wikipedia.org/wiki/Inositol#Isomers_and_structure">Inositol
     *      Isomers</a>
     */
    @Test public void inositols() {

        List<IAtomContainer> inositols = sdf("/data/hash/inositols.sdf", 9);

        // non-stereo non-perturbed hash generator
        MoleculeHashGenerator basic = new HashGeneratorMaker().elemental()
                                                              .depth(6)
                                                              .molecular();

        Set<Long> hashes = new HashSet<Long>(5);

        for (IAtomContainer inositol : inositols) {
            long hash = basic.generate(inositol);
            hashes.add(hash);
        }

        assertThat("all inositol isomers should hash to the same value",
                   hashes.size(), is(1));

        // stereo non-perturbed hash generator
        MoleculeHashGenerator stereo = new HashGeneratorMaker().elemental()
                                                               .depth(6)
                                                               .chiral()
                                                               .molecular();
        hashes.clear();

        for (IAtomContainer inositol : inositols) {
            long hash = stereo.generate(inositol);
            hashes.add(hash);
        }

        assertThat("all inositol isomers should hash to the same value",
                   hashes.size(), is(1));

        // stereo non-perturbed hash generator
        MoleculeHashGenerator perturbed = new HashGeneratorMaker().elemental()
                                                                  .depth(6)
                                                                  .chiral()
                                                                  .perturbed()
                                                                  .molecular();
        hashes.clear();

        for (IAtomContainer inositol : inositols) {
            long hash = perturbed.generate(inositol);
            hashes.add(hash);
        }

        assertThat("all inositol isomers should hash to different values",
                   hashes.size(), is(9));

    }

    private static String title(IAtomContainer mol) {
        return mol.getProperty(TITLE);
    }

    private static String nonEqMesg(IAtomContainer a, IAtomContainer b) {
        return title(a) + " and " + title(b) + " should have different hash codes";
    }

    private static String eqMesg(IAtomContainer a, IAtomContainer b) {
        return title(a) + " and " + title(b) + " should have the same hash codes";
    }

    /**
     * Utility for loading SDFs into a List.
     *
     * @param path absolute path to SDF (classpath)
     * @param exp  expected number of structures
     * @return list of structures
     */
    private List<IAtomContainer> sdf(String path, int exp) {
        InputStream in = getClass().getResourceAsStream(path);

        assertNotNull(path + " could not be found in classpath", in);

        IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
        IteratingSDFReader sdf = new IteratingSDFReader(in, builder, false);
        List<IAtomContainer> structures = new ArrayList<IAtomContainer>(exp);
        while (sdf.hasNext()) {
            IAtomContainer mol = sdf.next();
            try {
                AtomContainerManipulator
                        .percieveAtomTypesAndConfigureAtoms(mol);
                structures.add(mol);
            } catch (CDKException e) {
                System.err.println(e.getMessage());
            }
        }

        // help identify if the SDF reader messed up
        assertThat("unexpected number of structures",
                   structures.size(),
                   is(exp));

        return structures;
    }

}