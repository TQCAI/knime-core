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
 */

package org.knime.core.data.convert.datacell;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataType;
import org.knime.core.data.container.CellFactory;
import org.knime.core.data.convert.ConverterFactory;
import org.knime.core.data.filestore.FileStoreFactory;
import org.knime.core.node.ExecutionContext;

/**
 * Interface for all factory classes which create {@link JavaToDataCellConverter JavaToDataCellConverters}.
 *
 * You may extend this class and register the derived class under the extension point
 * "org.knime.core.JavaToDataCellConverter". It is recommended that you extend
 * {@link SimpleJavaToDataCellConverterFactory} if possible.
 *
 * @author Jonathan Hale, KNIME, Konstanz, Germany
 *
 * @param <S> Java type which can be converted by the {@link JavaToDataCellConverter}s created by this factory
 * @since 3.2
 * @see org.knime.core.data.convert
 * @see JavaToDataCellConverter
 * @see JavaToDataCellConverterRegistry
 */
public interface JavaToDataCellConverterFactory<S> extends ConverterFactory<Class<?>, DataType> {

    /**
     * Create an instance of a {@link JavaToDataCellConverter} which is able to convert instances of <code>S</code> into
     * {@link DataCell DataCells} with the {@link DataType} returned by {@link #getDestinationType()}.
     *
     * @param context {@link ExecutionContext} which may be used for creating {@link CellFactory}s.
     * @return a {@link JavaToDataCellConverter} instances
     * @see #create(FileStoreFactory)
     */
    public default JavaToDataCellConverter<S> create(final ExecutionContext context) {
        return create(FileStoreFactory.createFileStoreFactory(context));
    }

    /**
     * Create an instance of a {@link JavaToDataCellConverter} which is able to convert instances of <code>S</code> into
     * {@link DataCell DataCells} with the {@link DataType} returned by {@link #getDestinationType()}.
     *
     * @param fileStoreFactory {@link FileStoreFactory} which may be used for creating {@link CellFactory}s.
     * @return a {@link JavaToDataCellConverter} instances
     * @since 4.0
     */
    public JavaToDataCellConverter<S> create(final FileStoreFactory fileStoreFactory);

    /**
     * {@inheritDoc}
     *
     * Should contain at least the simple type name of the java type from which the data cell is created.
     * <p>
     * <b> Examples: </b> "InputStream", "Integer", "XML String", "SDF String"
     * </p>
     * s
     */
    @Override
    default String getName() {
        return "";
    }
}
