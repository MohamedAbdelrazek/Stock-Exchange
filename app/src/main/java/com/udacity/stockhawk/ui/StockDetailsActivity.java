package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.udacity.stockhawk.LineChartModel;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by mostafa_anter on 12/15/16.
 */
public class StockDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.chart)
    LineChart mChart;
    private static final int STOCK_LOADER = 0;
    private Uri stockUri;

    private boolean getDataOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        stockUri = getIntent().getData();
        getSupportLoaderManager().initLoader(STOCK_LOADER, null, this);

    }

    private void initChart(List<LineChartModel> dataObjects) {
        mChart.setViewPortOffsets(0, 0, 0, 0);
        // mChart.setBackgroundColor(Color.rgb(104, 241, 175));

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        mChart.setMaxHighlightDistance(300);

        XAxis x = mChart.getXAxis();
        x.setEnabled(false);

        YAxis y = mChart.getAxisLeft();
        y.setLabelCount(6, false);
        y.setTextColor(Color.WHITE);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisLineColor(Color.WHITE);

        mChart.getAxisRight().setEnabled(false);

        // add data
        setData(dataObjects);

        mChart.getLegend().setEnabled(false);

        mChart.animateXY(2000, 2000);

        mChart.invalidate();

    }

    private void setData(List<LineChartModel> dataObjects) {

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (LineChartModel data : dataObjects) {
            // turn your data into Entry objects
            yVals.add(new Entry(data.getHistory(), data.getPrice()));
        }

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals, "DataSet 1");

            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.2f);
            //set1.setDrawFilled(true);
            set1.setDrawCircles(false);
            set1.setLineWidth(1.8f);
            set1.setCircleRadius(4f);
            set1.setCircleColor(Color.WHITE);
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setColor(Color.WHITE);
            set1.setFillColor(Color.WHITE);
            set1.setFillAlpha(100);
            set1.setDrawHorizontalHighlightIndicator(false);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return -10;
                }
            });

            // create a data object with the datasets
            LineData data = new LineData(set1);
            data.setValueTextSize(9f);
            data.setDrawValues(false);

            // set data
            mChart.setData(data);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (stockUri != null) {
            return new CursorLoader(
                    this,
                    stockUri,
                    Contract.Quote.QUOTE_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        String history = data.getString(Contract.Quote.POSITION_HISTORY);


        CSVReader csvReader = new CSVReader(new StringReader(history), ',');
        //Set column mapping strategy
        List<LineChartModel> lineChartList = new ArrayList<>();

        // read line by line
        String[] record = null;

        try {
            while ((record = csvReader.readNext()) != null) {
                LineChartModel lineChartModel = new LineChartModel();
                lineChartModel.setHistory(Float.valueOf(record[0]));
                lineChartModel.setPrice(Float.valueOf(record[1]));
                lineChartList.add(lineChartModel);
            }
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!getDataOnce)
            initChart(lineChartList);

        getDataOnce = true;

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
