// Copyright 2017-2019, Schlumberger
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Copyright 2018 Schlumberger. All Rights Reserved.
 *
 */
package org.opengroup.osdu.crs.model.search.parser;

import java.util.ArrayList;
import java.util.List;

import org.opengroup.osdu.crs.model.SphericalBoundingBoxImpl;
import org.opengroup.osdu.crs.model.interfaces.SphericalBoundingBox;

/**
 * Created by ZMai on 7/19/2016.
 */
public class SpatialUtility {
    public static final double MinX = -180;
    public static final double MaxX = 180;
    public static final double MinY = -90;
    public static final double MaxY = 90;

    /**
     * Checks whether the given boundingBox, {@link SphericalBoundingBox}, is valid.
     * @param boundingBox spherical bounding box
     * @throws IllegalArgumentException an exception will be thrown if the bounding box is invalid.
     */
    public static void assertSphericalBoundingBox(SphericalBoundingBox boundingBox) throws IllegalArgumentException {
        if(boundingBox == null)
            throw new IllegalArgumentException("bounding box is null");

        StringBuilder builder = new StringBuilder();
        if(boundingBox.getLongitudeLeft() < MinX || boundingBox.getLongitudeLeft() > MaxX)
            builder.append("The leftX should not be outside the range [" + MinX + "," + MaxX + "]. ");
        if(boundingBox.getLatitudeLower() < MinY || boundingBox.getLatitudeLower() > MaxY)
            builder.append("The lowerY should not be outside the range [" + MinY + "," + MaxY + "]. ");
        if(boundingBox.getLongitudeRight() < MinX || boundingBox.getLongitudeRight() > MaxX)
            builder.append("The rightX should not be outside the range [" + MinX + "," + MaxX + "]. ");
        if(boundingBox.getLatitudeUpper() < MinY || boundingBox.getLatitudeUpper() > MaxY)
            builder.append("The lowerY should not be outside the range [" + MinY + "," + MaxY + "]. ");
        if(boundingBox.getLatitudeLower() > boundingBox.getLatitudeUpper())
            builder.append("The lowerY should not be larger than upperY.");
        if(builder.length() > 0)
            throw new IllegalArgumentException(builder.toString());
    }

    public static boolean isValidSphericalBoundingBox(SphericalBoundingBox boundingBox) {
        if(boundingBox == null)
            return false;

        if((boundingBox.getLongitudeLeft() < MinX || boundingBox.getLongitudeLeft() > MaxX) ||
           (boundingBox.getLatitudeLower() < MinY || boundingBox.getLatitudeLower() > MaxY) ||
           (boundingBox.getLongitudeRight() < MinX || boundingBox.getLongitudeRight() > MaxX) ||
           ((boundingBox.getLatitudeUpper() < MinY || boundingBox.getLatitudeUpper() > MaxY)) ||
           (boundingBox.getLatitudeLower() > boundingBox.getLatitudeUpper()))
            return false;
        return true;
    }

    /**
     * Create the bounding boxes for indexing and search from the given bounding box
     * @param boundingBox source bounding box
     * @return A list contains one or two bounding boxes
     * @throws IllegalArgumentException an exception will be thrown if the lowerY is larger than the upperY in the given bounding box.
     */
    public static List<SphericalBoundingBox> createIndexBoundingBoxes(SphericalBoundingBox boundingBox) throws IllegalArgumentException {
        // Normalize leftX and rightY
        double leftX = wrap(boundingBox.getLongitudeLeft(), MinX, MaxX);
        double rightX = wrap(boundingBox.getLongitudeRight(), MinX, MaxX);

        // Normalize lowerY and upperY
        double lowerY = wrap(boundingBox.getLatitudeLower(), MinY, MaxY);
        double upperY = wrap(boundingBox.getLatitudeUpper(), MinY, MaxY);
        if(lowerY > upperY) {
            throw new IllegalArgumentException("lowerY should not larger than upperY");
        }

        ArrayList<SphericalBoundingBox> boundingBoxes = new ArrayList<>();
        if(leftX > rightX) {
            //Contain the line 180/-180
            SphericalBoundingBox boundingBox1 = new SphericalBoundingBoxImpl(leftX, lowerY, MaxX, upperY);
            boundingBoxes.add(boundingBox1);

            SphericalBoundingBox boundingBox2 = new SphericalBoundingBoxImpl(MinX, lowerY, rightX, upperY);
            boundingBoxes.add(boundingBox2);
        }
        else {
            SphericalBoundingBox boundingBox1 = new SphericalBoundingBoxImpl(leftX, lowerY, rightX, upperY);
            boundingBoxes.add(boundingBox1);
        }
        return boundingBoxes;
    }

    private static double wrap(double value, double min, double max) {
        if(value < min)
            return min;
        if(value > max)
            return max;
        return value;
    }

}
