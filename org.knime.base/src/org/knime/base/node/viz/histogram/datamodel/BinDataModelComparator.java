/*
 * -------------------------------------------------------------------
 * This source code, its documentation and all appendant files
 * are protected by copyright law. All rights reserved.
 *
 * Copyright, 2003 - 2007
 * University of Konstanz, Germany
 * Chair for Bioinformatics and Information Mining (Prof. M. Berthold)
 * and KNIME GmbH, Konstanz, Germany
 *
 * You may not modify, publish, transmit, transfer or sell, reproduce,
 * create derivative works from, distribute, perform, display, or in
 * any way exploit any of the content, in whole or in part, except as
 * otherwise expressly permitted in writing by the copyright owner or
 * as specified in the license file distributed with this product.
 *
 * If you have any questions please contact the copyright holder:
 * website: www.knime.org
 * email: contact@knime.org
 * -------------------------------------------------------------------
 * 
 * History
 *    23.02.2007 (Tobias Koetter): created
 */

package org.knime.base.node.viz.histogram.datamodel;

import java.util.Comparator;


/**
 * 
 * @author Tobias Koetter, University of Konstanz
 */
public class BinDataModelComparator implements Comparator<InteractiveBinDataModel> {

    /**
     * This method compares the caption of two bins and orders them in natural
     * order.
     */
    public static final int COMPARE_CAPTION = 1;
    
    /**
     * This method compares the boundaries of a bin and orders from lowest
     * upper bound to highest upper bound.
     */
    public static final int COMPARE_BOUNDARIES = 2;
    
    private final int m_compareMethod;
    
    /**Constructor for class BinDataModelComparator.
     * @param compareMethod the method used to compare
     * @see #COMPARE_BOUNDARIES
     * @see #COMPARE_CAPTION
     */
    public BinDataModelComparator(final int compareMethod) {
        m_compareMethod = compareMethod;
    }
    
    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(final InteractiveBinDataModel o1, final InteractiveBinDataModel o2) {
        switch (m_compareMethod) {
            case COMPARE_CAPTION:
                return o1.getXAxisCaption().compareTo(o2.getXAxisCaption());
            case COMPARE_BOUNDARIES:
                return Double.compare(o1.getUpperBound(), o2.getUpperBound());
            default:
                return o1.getXAxisCaption().compareTo(o2.getXAxisCaption());
        }
    }
        
}
