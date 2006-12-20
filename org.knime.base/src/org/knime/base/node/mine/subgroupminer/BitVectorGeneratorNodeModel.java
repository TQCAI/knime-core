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
 *   06.12.2005 (dill): created
 */
package org.knime.base.node.mine.subgroupminer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.knime.base.data.append.column.AppendedColumnRow;
import org.knime.base.data.bitvector.BitString2BitVectorCellFactory;
import org.knime.base.data.bitvector.BitVectorCell;
import org.knime.base.data.bitvector.BitVectorCellFactory;
import org.knime.base.data.bitvector.Hex2BitVectorCellFactory;
import org.knime.base.data.bitvector.IdString2BitVectorCellFactory;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DoubleValue;
import org.knime.core.data.container.ColumnRearranger;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettings;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 * The BitvectorGenerator translates all values above or equal to a given
 * threshold to a set bit, values below that threshold to bits set to zero.
 * Thus, an n-column input will be translated to a 1-column table, where each
 * column contains a bitvector of length n.
 * 
 * @author Fabian Dill, University of Konstanz
 */
public class BitVectorGeneratorNodeModel extends NodeModel {
    /**
     * Represents the string types that can be parsed.
     * 
     * @author Fabian Dill, University of Konstanz
     */
    public enum STRING_TYPES {
        /** A hexadecimal representation of bits. */
        HEX,
        /**
         * A string of ids, where every id indicates the position in the bitset
         * to set.
         */
        ID,
        /** A string of '0' and '1'. */
        BIT;
    }

    // private static final NodeLogger logger
    // = NodeLogger.getLogger(BitVectorGeneratorNodeModel.class);

    /** Config key for the threshold. */
    public static final String CFG_THRESHOLD = "THRESHOLD";

    /** Config key if a StringCell should be parsed. */
    public static final String CFG_FROM_STRING = "FROM_STRING";

    /** Config key for the StringColumn to parse. */
    public static final String CFG_STRING_COLUMN = "STRING_COLUMN";

    /** Config key for the type of string (Hex, Id, Bit). */
    public static final String CFG_STRING_TYPE = "stringType";

    /** Config key whether the threshold is used for the mean. */
    public static final String CFG_USE_MEAN = "useMean";

    /** Config key for the mean percentage. */
    public static final String CFG_MEAN_THRESHOLD = "meanThreshold";

    /** Default value for the threshold. */
    public static final double DEFAULT_THRESHOLD = 1.0;
    
    /** Flag whether column(s) should be replaced or not. */ 
    public static final String CFG_REPLACE = "replace";

    private double m_threshold = DEFAULT_THRESHOLD;

    private boolean m_fromString = false;

    private boolean m_useMean = false;

    private int m_meanPercentage = 100;

    private STRING_TYPES m_type = STRING_TYPES.BIT;

    private String m_stringColumn;

    private static final String FILE_NAME = "bitVectorParams";

    private int m_nrOfProcessedRows = 0;

    private int m_bitVectorLength;

    private int m_totalNrOf1s;

    private int m_totalNrOf0s;

    private static final String INT_CFG_ROWS = "nrOfRows";

    private static final String INT_CFG_LENGTH = "bitVectorLength";

    private static final String INT_CFG_NR_ZEROS = "nrOfZeros";

    private static final String INT_CFG_NR_ONES = "nrOfOnes";
    
    private BitVectorCellFactory m_factory;
    
    private boolean m_replace = false;

    /**
     * Creates an instance of the BitVectorGeneratorNodeModel with one inport
     * and one outport. The numerical values from the inport are translated to
     * bivectors in the output.
     */
    public BitVectorGeneratorNodeModel() {
        super(1, 1);
    }

    /**
     * @return the number of processed rows
     */
    public int getNumberOfProcessedRows() {
        return m_nrOfProcessedRows;
    }

    /**
     * 
     * @return the number of 1s generated
     */
    public int getTotalNrOf1s() {
        if (!m_fromString) {
            return m_totalNrOf1s;
        }
        return m_factory.getNumberOfSetBits();
    }

    /**
     * 
     * @return the number of 0s generated
     */
    public int getTotalNrOf0s() {
        if (!m_fromString) {
            return m_totalNrOf0s;
        }
        return m_factory.getNumberOfNotSetBits();
    }

    /**
     * 
     * @return the (fixed) length of the resulting bitvector
     */
    public int getBitVectorLength() {
        return m_bitVectorLength;
    }

    /**
     * @see org.knime.core.node.NodeModel#saveSettingsTo( NodeSettingsWO)
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        settings.addDouble(CFG_THRESHOLD, m_threshold);
        settings.addBoolean(CFG_FROM_STRING, m_fromString);
        settings.addString(CFG_STRING_COLUMN, m_stringColumn);
        settings.addBoolean(CFG_USE_MEAN, m_useMean);
        settings.addInt(CFG_MEAN_THRESHOLD, m_meanPercentage);
        settings.addString(CFG_STRING_TYPE, m_type.name());
        settings.addBoolean(CFG_REPLACE, m_replace);
    }

    /**
     * @see org.knime.core.node.NodeModel#validateSettings( NodeSettingsRO)
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        settings.getDouble(CFG_THRESHOLD);
        settings.getBoolean(CFG_USE_MEAN);
        settings.getInt(CFG_MEAN_THRESHOLD);
        boolean fromString = settings.getBoolean(CFG_FROM_STRING);
        String stringCol = settings.getString(CFG_STRING_COLUMN);
        if (fromString && stringCol == null) {
            throw new InvalidSettingsException(
                    "A String column must be specified!");
        }
        settings.getString(CFG_STRING_TYPE);
        settings.getBoolean(CFG_REPLACE);
    }

    /**
     * @see org.knime.core.node.NodeModel#loadValidatedSettingsFrom(
     *      NodeSettingsRO)
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        m_threshold = settings.getDouble(CFG_THRESHOLD);
        m_fromString = settings.getBoolean(CFG_FROM_STRING);
        m_stringColumn = settings.getString(CFG_STRING_COLUMN);
        m_useMean = settings.getBoolean(CFG_USE_MEAN);
        m_meanPercentage = settings.getInt(CFG_MEAN_THRESHOLD);
        m_type = STRING_TYPES.valueOf(settings.getString(CFG_STRING_TYPE));
        m_replace = settings.getBoolean(CFG_REPLACE);
    }

    private double[] calculateMeanValues(final DataTable input) {
        double[] meanValues = new double[input.getDataTableSpec()
                .getNumColumns()];
        int nrOfRows = 0;
        for (DataRow row : input) {
            for (int i = 0; i < row.getNumCells(); i++) {
                if (row.getCell(i).isMissing()
                 || !row.getCell(i).getType().isCompatible(DoubleValue.class)) {
                    continue;
                }
                meanValues[i] += ((DoubleValue)row.getCell(i)).getDoubleValue();
            }
            nrOfRows++;
        }
        for (int i = 0; i < meanValues.length; i++) {
            meanValues[i] = meanValues[i] / nrOfRows;
        }
        return meanValues;
    }

    /**
     * @see org.knime.core.node.NodeModel#execute( BufferedDataTable[],
     *      ExecutionContext)
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
        BufferedDataTable numericValues = inData[0];
        List<String> nameMapping = new ArrayList<String>();
        m_nrOfProcessedRows = numericValues.getRowCount();
        for (int i = 0; i < numericValues.getDataTableSpec().getNumColumns(); 
            i++) {
            nameMapping.add(numericValues.getDataTableSpec().getColumnSpec(i)
                    .getName().toString());
        }

        if (m_fromString) {
            int stringColumnPos = inData[0].getDataTableSpec().findColumnIndex(
                    m_stringColumn);
            BufferedDataTable[] bfdt = createBitVectorsFromStrings(
                    inData[0], stringColumnPos, exec);
            if (!m_factory.wasSuccessful()) {
                throw new IllegalArgumentException(
                        "Nothing to convert in String column: " 
                        + inData[0].getDataTableSpec().getColumnSpec(
                                stringColumnPos).getName()
                        + ". Check NodeDescription for correct input formats!");
            }
            return bfdt;

        }
        double[] meanValues = new double[0];
        double meanFactor = m_meanPercentage / 100.0;
        if (m_useMean) {
            meanValues = calculateMeanValues(inData[0]);

        }
        m_bitVectorLength = inData[0].getDataTableSpec().getNumColumns();
        long rowNr = 0;
        long rowCnt = numericValues.getRowCount();
        BufferedDataContainer cont = exec.createDataContainer(
                createNumericOutputSpec(inData[0].getDataTableSpec()));
        for (DataRow currRow : numericValues) {
            exec.checkCanceled();
            exec.setProgress((double)rowNr / (double)rowCnt,
                    "Processing row nr.:" + rowNr);
            rowNr++;
            BitSet currBitSet = new BitSet(currRow.getNumCells());
            for (int i = 0; i < currRow.getNumCells(); i++) {
                if (!currRow.getCell(i).getType().isCompatible(
                        DoubleValue.class)) {
                    continue;
                }
                if (currRow.getCell(i).isMissing()) {
                    m_totalNrOf0s++;
                    continue;
                }
                double currValue = ((DoubleValue)currRow.getCell(i))
                        .getDoubleValue();
                if (!m_useMean) {
                    if (currValue >= m_threshold) {
                        currBitSet.set(i);
                        m_totalNrOf1s++;
                    } else {
                        m_totalNrOf0s++;
                    }
                } else {
                    if (currValue >= (meanFactor * meanValues[i])) {
                        currBitSet.set(i);
                        m_totalNrOf1s++;
                    } else {
                        m_totalNrOf0s++;
                    }
                }
            }
            DataRow bitSetRow;
            if (m_replace) {
                bitSetRow = new DefaultRow(currRow.getKey(),
                    new DataCell[]{new BitVectorCell(currBitSet, currRow
                            .getNumCells(), nameMapping)});
            } else {
                bitSetRow = new AppendedColumnRow(currRow,
                        new BitVectorCell(currBitSet, currRow.getNumCells(), 
                                nameMapping));                
            }
            cont.addRowToTable(bitSetRow);
        }
        cont.close();
        return new BufferedDataTable[]{cont.getTable()};
    }

    private BufferedDataTable[] createBitVectorsFromStrings(
            final BufferedDataTable data,
            final int stringColIndex, final ExecutionContext exec) 
            throws CanceledExecutionException {
        ColumnRearranger c = createColumnRearranger(data.getDataTableSpec(),
                stringColIndex);
        BufferedDataTable out = exec.createColumnRearrangeTable(data, c, exec);
        return new BufferedDataTable[]{out};
    }
    
    private ColumnRearranger createColumnRearranger(final DataTableSpec spec, 
            final int colIdx) {
        // BW: fixed here locally: the annotation and the column name
        // are taken from input spec (21 Sep 2006)
        DataColumnSpecCreator creator = new DataColumnSpecCreator(
                spec.getColumnSpec(colIdx));
        creator.setDomain(null);
        creator.setType(BitVectorCell.TYPE);
        if (!m_replace) {
            creator.setName(spec.getColumnSpec(colIdx).getName() + "_bits");
        }
        if (m_type.equals(STRING_TYPES.BIT)) {
            m_factory = new BitString2BitVectorCellFactory(creator.createSpec(),
                    colIdx);
        } else if (m_type.equals(STRING_TYPES.HEX)) {
            m_factory = new Hex2BitVectorCellFactory(creator.createSpec(),
                    colIdx);
        } else if (m_type.equals(STRING_TYPES.ID)) {
            m_factory = new IdString2BitVectorCellFactory(creator.createSpec(),
                    colIdx);
        }
        ColumnRearranger c = new ColumnRearranger(spec);
        if (m_replace) {
            c.replace(m_factory, colIdx);
        } else {
            c.append(m_factory);
        }
        return c; 
    }


    /**
     * @see org.knime.core.node.NodeModel#reset()
     */
    @Override
    protected void reset() {
    }

    /**
     * Assume to get numeric data only. Output is one column of type BitVector.
     * 
     * @see org.knime.core.node.NodeModel#configure(
     *      org.knime.core.data.DataTableSpec[])
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
        DataTableSpec spec = inSpecs[0];
        if (!m_fromString) {
            boolean hasNumericCol = false;
            for (int i = 0; i < spec.getNumColumns(); i++) {
                if (spec.getColumnSpec(i).getType().isCompatible(
                        DoubleValue.class)) {
                    hasNumericCol = true;
                    break;
                }
            }
            if (!hasNumericCol) {
                throw new InvalidSettingsException("No numeric column found");
            }
        } else {
            if (!spec.containsName(m_stringColumn)) {
                throw new InvalidSettingsException("Selected column "
                        + m_stringColumn + " not in the input specs");
            }
        }
        if (m_fromString) {
            int stringColIdx = inSpecs[0].findColumnIndex(m_stringColumn);
            ColumnRearranger c = createColumnRearranger(
                    inSpecs[0], stringColIdx);
            return new DataTableSpec[]{c.createSpec()};
        } else {
            return new DataTableSpec[]{createNumericOutputSpec(inSpecs[0])};
        }
    }
    
    
    private DataTableSpec createNumericOutputSpec(final DataTableSpec spec) {
        DataColumnSpecCreator creator = new DataColumnSpecCreator(
                "BitVectors", BitVectorCell.TYPE);
        DataTableSpec newSpec = new DataTableSpec(creator.createSpec()); 
        if (m_replace) {
            return newSpec;
        } else {
            return new DataTableSpec(spec, newSpec);
        }
    }

    /**
     * @see org.knime.core.node.NodeModel
     *      #loadInternals(java.io.File,ExecutionMonitor)
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException {
        File f = new File(internDir, FILE_NAME);
        FileInputStream fis = new FileInputStream(f);
        NodeSettingsRO internSettings = NodeSettings.loadFromXML(fis);
        try {
            m_nrOfProcessedRows = internSettings.getInt(INT_CFG_ROWS);
            m_bitVectorLength = internSettings.getInt(INT_CFG_LENGTH);
            m_totalNrOf0s = internSettings.getInt(INT_CFG_NR_ZEROS);
            m_totalNrOf1s = internSettings.getInt(INT_CFG_NR_ONES);
        } catch (InvalidSettingsException ise) {
            throw new IOException(ise.getMessage());
        }
    }

    /**
     * @see org.knime.core.node.NodeModel
     *      #saveInternals(java.io.File,ExecutionMonitor)
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException {
        NodeSettings internSettings = new NodeSettings(FILE_NAME);
        internSettings.addInt(INT_CFG_ROWS, m_nrOfProcessedRows);
        internSettings.addInt(INT_CFG_LENGTH, m_bitVectorLength);
        internSettings.addInt(INT_CFG_NR_ZEROS, m_totalNrOf0s);
        internSettings.addInt(INT_CFG_NR_ONES, m_totalNrOf1s);
        File f = new File(internDir, FILE_NAME);
        FileOutputStream fos = new FileOutputStream(f);
        internSettings.saveToXML(fos);
    }
}
