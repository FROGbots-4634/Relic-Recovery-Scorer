package net.frogbots.relicrecoveryscorecalculator.backend.export;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import net.frogbots.relicrecoveryscorecalculator.R;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Adapter for showing a simple list of directories
 */
public class ExportDirAdapter extends ArrayAdapter<File>
{
    private int mPosition = -1;

    public ExportDirAdapter (Context context, String directory) throws IOException
    {
        super(context, R.layout.export_file_item, android.R.id.text1);

        File[] files = new File(directory).listFiles();
        if (files == null)
        {
            throw new IOException(String.format("unable to read %s", directory));
        }

        // Sort the list of directories by name
        Arrays.sort(files, new Comparator<File>()
        {
            @Override
            public int compare (File o1, File o2)
            {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });

        // Append all of the directories
        //add(new File(".."));
        for (File file : files)
        {
            if (!file.isDirectory())
            {
                if(file.getName().endsWith(".csv"))
                {
                    add(file);
                }
            }
        }
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent)
    {
        View view = super.getView(position, convertView, parent);
        File file = getItem(position);
        ((ImageView) view.findViewById(android.R.id.icon)).setImageResource(R.drawable.ic_file);
        //noinspection ConstantConditions
        ((TextView) view.findViewById(android.R.id.text1)).setText(file.getName());
        /*view.setBackgroundColor(
                position == mPosition ?
                        ContextCompat.getColor(getContext(), R.color.colorAccent) :
                        Color.TRANSPARENT
        );*/
        return view;
    }

    void setSelection (int position)
    {
        mPosition = position;
    }

    /**
     * Obtain the currently selected path
     */
    String getSelection ()
    {
        //noinspection ConstantConditions
        return 0 <= mPosition && mPosition < getCount() ?
                getItem(mPosition).getAbsolutePath() : null;
    }
}