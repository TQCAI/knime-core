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
 *   Dec 15, 2006 (wiswedel): created
 */
package org.knime.core.data.container;

import java.io.IOException;

import org.knime.core.data.DataCell;
import org.knime.core.data.container.BlobDataCell.BlobAddress;

/**
 * 
 * @author Bernd Wiswedel, University of Konstanz
 */
final class BlobWrapperDataCell extends DataCell {
    
    private final Buffer m_buffer;
    private final BlobAddress m_blobAddress;
    private final Class<? extends BlobDataCell> m_blobClass;
    private BlobDataCell m_cell;
    
    BlobWrapperDataCell(final Buffer b, final BlobAddress ba,
            final Class<? extends BlobDataCell> cl) {
        assert b.getBufferID() == ba.getBufferID();
        m_buffer = b;
        m_blobAddress = ba;
        m_blobClass = cl;
    }
    
    BlobDataCell getCell() {
        if (m_cell == null) {
            try {
                m_cell = m_buffer.readBlobDataCell(m_blobAddress, m_blobClass);
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
        return m_cell;
    }
    
    BlobAddress getAddress() {
        return m_blobAddress;
    }
    
    Class<? extends BlobDataCell> getBlobClass() {
        return m_blobClass;
    }

    /**
     * @see DataCell#equalsDataCell(DataCell)
     */
    @Override
    protected boolean equalsDataCell(final DataCell dc) {
        assert false : "equalsDataCell must not be called on Blob wrapper";
        return getCell().equals(dc);
    }

    /**
     * @see org.knime.core.data.DataCell#hashCode()
     */
    @Override
    public int hashCode() {
        assert false : "hasCode must not be called on Blob wrapper";
        return getCell().hashCode();
    }

    /**
     * @see org.knime.core.data.DataCell#toString()
     */
    @Override
    public String toString() {
        return getCell().toString();
    }

}
