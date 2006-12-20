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
 *   19.04.2005 (cebron): created
 */
package org.knime.base.node.preproc.normalize;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.knime.base.data.normalize.AffineTransTable;
import org.knime.base.data.normalize.Normalizer;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DoubleValue;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.ModelContent;
import org.knime.core.node.ModelContentRO;
import org.knime.core.node.ModelContentWO;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 * The NormalizeNodeModel uses the Normalizer to normalize the input DataTable.
 * 
 * @see Normalizer
 * @author Nicolas Cebron, University of Konstanz
 */
public class NormalizerNodeModel extends NodeModel {

    /**
     * Key to store the new minimum value (in minmax mode).
     */
    public static final String NEWMIN_KEY = "newmin";

    /**
     * Key to store the new maximum value (in minmax mode).
     */
    public static final String NEWMAX_KEY = "newmax";

    /**
     * Key to store the mode.
     */
    public static final String MODE_KEY = "mode";

    /**
     * Key to store the columns to use.
     */
    public static final String COLUMNS_KEY = "columns";

    /**
     * No Normalization mode.
     */
    public static final int NONORM_MODE = 0;
   
    /**
     * MINMAX mode.
     */
    public static final int MINMAX_MODE = 1;

    /**
     * ZSCORE mode.
     */
    public static final int ZSCORE_MODE = 2;

    /**
     * DECIMAL SCALING mode.
     */
    public static final int DECIMALSCALING_MODE = 3;

    /*
     * Default mode is NONORM mode
     */
    private int m_mode = NONORM_MODE;

    /*
     * Default minimum zero
     */
    private double m_min = 0;

    /*
     * Default maximum one
     */
    private double m_max = 1;

    /*
     * Columns to use for normalization.
     */
    private String[] m_columns;
    
    /**
     * The model content.
     */
    private ModelContentRO m_content;
    
    /** The config key under which the model is stored. */
    static final String CFG_MODEL_NAME = "normalize";

    /**
     * Creates an new normalizer using the given number of in- and output data
     * and model ports.
     * @param dataIns number data input ports
     * @param dataOuts number data output ports
     * @param modelIns number model input ports  
     * @param modelOuts number model output ports
     */
    public NormalizerNodeModel(final int dataIns, final int dataOuts, 
            final int modelIns, final int modelOuts) {
        super(dataIns, dataOuts, modelIns, modelOuts);
    }
    
    /**
     * All {@link org.knime.core.data.def.IntCell} columns are converted to
     * {@link org.knime.core.data.def.DoubleCell} columns.
     * 
     * @see NodeModel#configure(DataTableSpec[])
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
        if (inSpecs[0].getNumColumns() > 0) {
            if (m_columns != null) {
                // sanity check: selected columns in actual spec?
                for (String name : m_columns) {
                    if (inSpecs[0].findColumnIndex(name) < 0) {
                        throw new InvalidSettingsException("Could not"
                                + " find " + name.toString() + " in TableSpec");
                    }
                }
            } else {
                // no selected cols: include all columns for selection.
                int nrcols = inSpecs[0].getNumColumns();
                Vector<String> poscolumns = new Vector<String>();
                for (int i = 0; i < nrcols; i++) {
                    if (inSpecs[0].getColumnSpec(i).getType().isCompatible(
                            DoubleValue.class)) {
                        poscolumns.add(inSpecs[0].getColumnSpec(i).getName());
                    }
                }
                m_columns = new String[poscolumns.size()];
                m_columns = poscolumns.toArray(m_columns);
            }
            return new DataTableSpec[]{Normalizer.generateNewSpec(inSpecs[0],
                    m_columns)};
        }
        return null;
    }

    /**
     * New normalized {@link org.knime.core.data.DataTable} is created depending
     * on the mode.
     * 
     * @see NodeModel#execute(BufferedDataTable[],ExecutionContext)
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
        Normalizer ntable = new Normalizer(inData[0], m_columns);
        AffineTransTable outTable;
        m_content = new ModelContent(CFG_MODEL_NAME);
        switch (m_mode) {
        case NONORM_MODE:
            return inData;
        case MINMAX_MODE:
            outTable = ntable.doMinMaxNorm(m_max, m_min, exec);
            break;
        case ZSCORE_MODE:
            outTable = ntable.doZScoreNorm(exec);
            break;
        case DECIMALSCALING_MODE:
            outTable = ntable.doDecimalScaling(exec);
            break;
        default:
            throw new Exception("No mode set");
        }
        
        outTable.save((ModelContent)m_content);
        BufferedDataTable bft = exec.createBufferedDataTable(outTable, exec);
        return new BufferedDataTable[]{bft};
    }
    
    /**
     * @see NodeModel#saveModelContent(int, ModelContentWO)
     */
    @Override
    protected void saveModelContent(final int index, 
            final ModelContentWO predParams) throws InvalidSettingsException {
        ModelContentWO sub = predParams.addModelContent(CFG_MODEL_NAME);
        m_content.copyTo(sub);
    }
    
    /**
     * @see org.knime.core.node.NodeModel#loadInternals (java.io.File,
     *      org.knime.core.node.ExecutionMonitor)
     */
    @Override
    protected void loadInternals(final File nodeInternDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    }

    /**
     * 
     * @see org.knime.core.node.NodeModel#saveInternals (java.io.File,
     *      org.knime.core.node.ExecutionMonitor)
     */
    @Override
    protected void saveInternals(final File nodeInternDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    }

    /**
     * @see NodeModel#loadValidatedSettingsFrom(NodeSettingsRO)
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        m_mode = settings.getInt(MODE_KEY);
        m_min = settings.getDouble(NEWMIN_KEY);
        m_max = settings.getDouble(NEWMAX_KEY);
        m_columns = settings.getStringArray(COLUMNS_KEY);
    }

    /**
     * @see NodeModel#reset()
     */
    @Override
    protected void reset() {
        m_content = null;
    }

    /**
     * @see NodeModel#saveSettingsTo(NodeSettingsWO)
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        settings.addInt(MODE_KEY, m_mode);
        settings.addDouble(NEWMIN_KEY, m_min);
        settings.addDouble(NEWMAX_KEY, m_max);
        if (m_columns != null) {
            settings.addStringArray(COLUMNS_KEY, m_columns);
        }

    }

    /**
     * @see NodeModel#validateSettings(NodeSettingsRO)
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        int mode = settings.getInt(MODE_KEY);
        switch (mode) {
        case NONORM_MODE:
        case MINMAX_MODE:
        case ZSCORE_MODE:
        case DECIMALSCALING_MODE:
            break;
        default:
            throw new InvalidSettingsException("INVALID MODE");
        }
    }
}
