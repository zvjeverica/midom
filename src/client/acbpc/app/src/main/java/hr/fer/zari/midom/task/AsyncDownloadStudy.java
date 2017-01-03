package hr.fer.zari.midom.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import hr.fer.zari.midom.dialogs.DialogLoading;
import hr.fer.zari.midom.utils.Constants;
import hr.fer.zari.midom.utils.MidomUtils;

/**
 * AsyncTask for downloading study.
 * Downloading file from given url in constructor,
 * and unzipping it folder "/temp-download"
 *
 *
 * Currently can't show progress because server returns -1,
 * maybe add later
 *
 */
public class AsyncDownloadStudy extends AsyncTask<Void, Void, Void> {

    public static final String TAG = AsyncDownloadStudy.class.getSimpleName();

    private Activity activity;
    private URL url;
    private int ID;
    //private AsyncLoadImage.OnTaskCompleted listener;
    private DialogLoading dialogLoading;
    private unzipCompleted listener;

//    private DialogDownloading dialogDownloading;

    public AsyncDownloadStudy(Activity activity, int ID) throws MalformedURLException {
        this.activity = activity;
        this.url = new URL(Constants.GET_UNCOMP_STUDY + ID);
        this.ID = ID;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        MidomUtils.lockOrientation(activity);
        Log.d(TAG, "Started onPreExecute");
        dialogLoading = new DialogLoading();
        dialogLoading.setTitle("Downloading study");
        dialogLoading.show(activity.getFragmentManager(), TAG);

//        dialogDownloading = new DialogDownloading();
//        dialogDownloading.show(activity.getFragmentManager(), TAG);
        // create folder for downloading images
        File downloadFolder = new File(Constants.ZIP_DOWNLOAD_LOCATION);
        if (!downloadFolder.exists()) {
            boolean created = downloadFolder.mkdirs();
            if (!created) {
                Log.e(TAG, "cannot create the folder for downloading images");
            }
        }
    }

    @Override
    protected Void doInBackground(Void... v) {
        Log.d(TAG, "Started doInBackground");

        InputStream input = null;
        OutputStream output = null;
        try {
            URLConnection connection = url.openConnection();
            connection.connect();


            // if -1 server dosen't return size of file
            int lengthOfFile = connection.getContentLength();
            Log.d(TAG, "Length of file = " + lengthOfFile);

            // input stream to read file - with 8k buffer
            input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write file
            output = new FileOutputStream(Constants.ZIP_DOWNLOAD_LOCATION + "/"
                    + Constants.ZIP_DOWNLOAD_NAME + ID + ".zip");

            byte data[] = new byte[1024];
            //long total = 0;
            int count;

            while ((count = input.read(data)) != -1) {
                //total += count;
                //calculate the percentage of downloaded file
                if (lengthOfFile > 0) {
                    //progress = (int) ((total * 100) / lengthOfFile);
                    //publishProgress();
                }
                // writing data to file
                output.write(data, 0, count);
            }
            unzipFunction(Constants.ZIP_DOWNLOAD_LOCATION + "/" + Constants.ZIP_DOWNLOAD_NAME + ID + ".zip",
                    Constants.ZIP_EXTRACT);
        } catch (Exception e) {
            Log.e(TAG, "Error with downloading file");
        } finally {
            try {
                if (output != null) {
                    output.flush();
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... v) {
        super.onProgressUpdate(v);
    }

    /**
     * First unzips downloaded file and then start AsyncTask to load and show image.
     * @param   v   Void nothing
     */
    @Override
    protected void onPostExecute(Void v) {
        super.onPostExecute(v);
        dialogLoading.dismiss();
        MidomUtils.unLockOrientation(activity);
        listener.unzipFinished();
    }


    /**
     * Function for unzipping file.zip
     * @param zipFile   Path to file.zip
     * @param destinationFolder Path for extraction
     */
    private void unzipFunction(String zipFile, String destinationFolder) {
        File directory = new File(destinationFolder);

        // if the output directory doesn't exist, create it
        if(!directory.exists()) {
            directory.mkdirs();
        }

        // buffer for read and write data to file
        byte[] buffer = new byte[4096];

        try {
            FileInputStream fInput = new FileInputStream(zipFile);
            BufferedInputStream bInput = new BufferedInputStream(fInput, buffer.length);
            ZipInputStream zipInput = new ZipInputStream(bInput);

            ZipEntry entry = zipInput.getNextEntry();

            while(entry != null){
                String entryName = entry.getName();
                File file = new File(destinationFolder + File.separator + entryName);

                System.out.println("Unzip file " + entryName + " to " + file.getAbsolutePath());

                // create the directories of the zip directory
                if(entry.isDirectory()) {
                    File newDir = new File(file.getAbsolutePath());
                    if(!newDir.exists()) {
                        boolean success = newDir.mkdirs();
                        if(!success) {
                            System.out.println("Problem creating Folder");
                        }
                    }
                }
                else {
                    FileOutputStream fOutput = new FileOutputStream(file);
                    BufferedOutputStream bOutput = new BufferedOutputStream(fOutput, buffer.length);
                    int count;
                    while ((count = zipInput.read(buffer)) > 0) {
                        // write 'count' bytes to the file output stream
                        bOutput.write(buffer, 0, count);
                    }
                    bOutput.flush();
                    bOutput.close();

                    fOutput.flush();
                    fOutput.close();
                }
                // close ZipEntry and take the next one
                zipInput.closeEntry();
                entry = zipInput.getNextEntry();
            }

            // close the last ZipEntry
            zipInput.closeEntry();

            zipInput.close();
            fInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface unzipCompleted {
        void unzipFinished();
    }

    public void setListener(unzipCompleted listener) {
        this.listener = listener;
    }

}
