package waterdetection.usf.waterdetectionandroid.detection.modes;

import android.content.res.AssetManager;

import org.opencv.core.Mat;

import java.io.File;

import waterdetection.usf.waterdetectionandroid.tfclassification.Classifier;
import waterdetection.usf.waterdetectionandroid.tfclassification.ClassifierFactory;
import waterdetection.usf.waterdetectionandroid.tfclassification.FileUtils;

class FloorDetector implements Detector {
    private Classifier floorClassifier;
    private ImgUtils imgUtils = new ImgUtils();
    private FileUtils fileUtils = new FileUtils();
    private File albumStorageDir;
    private boolean isExternalStorageWritable;

    public FloorDetector(AssetManager assetManager, File albubStorageDir, boolean isExternalStorageWritable) {
        this.floorClassifier = ClassifierFactory.createFloorDetectionClassifier(assetManager);
        this.albumStorageDir = albubStorageDir;
        this.isExternalStorageWritable = isExternalStorageWritable;
    }

    @Override
    public Mat performDetection(Mat originalImage) {
        Long startTime = System.currentTimeMillis();
        float[] inputValues = imgUtils.convertMatToFloatArr(originalImage);
        Long startFloor = System.currentTimeMillis();
        float[] superpixels = floorClassifier.classifyImage(inputValues); //Perform the inference on the input image
        Long endFloor = System.currentTimeMillis();
        Mat finalImage = imgUtils.paintOriginalImage(superpixels, originalImage, false);
        Long endTime = System.currentTimeMillis();
        if (isExternalStorageWritable) {
            fileUtils.mSaveData("TimesFloorOriginal.txt", (endFloor - startFloor) + ";" + 0 + ";" + (endTime-startTime), albumStorageDir);
        }
        return finalImage;
    }
}
