/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME AG, Zurich, Switzerland
 *  Website: http://www.knime.com; Email: contact@knime.com
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME AG herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ---------------------------------------------------------------------
 *
 * History
 *   Mar 26, 2020 (marcel): created
 */
package org.knime.core.data.container.newapi;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.container.newapi.store.PrimitiveRow;
import org.knime.core.data.container.newapi.store.Store;
import org.knime.core.data.container.newapi.store.StoreReadAccess;
import org.knime.core.data.container.newapi.store.StoreWriteAccess;

/*
 * - Abstracts caching and life-cycle management (closing, iterators, data-row) from store implementor (Arrow, Parquet, etc.)
 * - Implementors provide batches and possibly buffer them
 */
public class DefaultTableAccessible implements TableAccessible {

    private final DataTableSpec m_spec;

    // TODO instantiate the right store.
    private Store m_store;

    public DefaultTableAccessible(final DataTableSpec spec) {
        m_spec = spec;
    }

    @SuppressWarnings("resource")
    @Override
    public TableWriteAccess createWriteAccess() {
        final StoreWriteAccess m_storeWriteAccess = m_store.createWriteAccess();

        return new TableWriteAccess() {

            @Override
            public void add(final DataRow row) {
                // TODO some associated proxy with row/type mapping. updated be "row". object can be reused.
                // TODO proxy can be re-used
                m_storeWriteAccess.accept(null);
            }

            @Override
            public void close() throws Exception {
                m_storeWriteAccess.close();
            }
        };
    }

    @SuppressWarnings("resource")
    @Override
    public TableReadAccess createReadAccess() {
        // TODO pass config
        final StoreReadAccess m_storeReadAccess = m_store.createReadAccess(null);
        return new TableReadAccess() {

            @Override
            public boolean hasNext() {
                return m_storeReadAccess.hasNext();
            }

            @Override
            public DataRow next() {
                PrimitiveRow next = m_storeReadAccess.next();
                // type mapping of 'next' to DataRow
                return null;
            }

            @Override
            public void close() throws Exception {
                m_storeReadAccess.close();
            }
        };
    }

    @Override
    public void destroy() {
        m_store.destroy();
    }
}
