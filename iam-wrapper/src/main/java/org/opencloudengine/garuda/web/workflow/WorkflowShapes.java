package org.opencloudengine.garuda.web.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by uengine on 2016. 7. 4..
 */
public class WorkflowShapes {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(WorkflowShapes.class);
    public static List<WorkflowShapes> shapesProvidedList = new ArrayList<>();

    static {

        shapesProvidedList.add(
                new WorkflowShapes(
                        "/service-console/resources/opengraph/router/Request.png",
                        "GEOM",
                        "OG.shape.router.Request",
                        "Request",
                        "Request",
                        50, 50));

        shapesProvidedList.add(
                new WorkflowShapes(
                        "/service-console/resources/opengraph/router/Authentication.png",
                        "GEOM",
                        "OG.shape.router.Authentication",
                        "Authentication",
                        "YES,NO",
                        50, 50));
//        shapesProvidedList.add(
//                new WorkflowShapes(
//                        "/service-console/resources/opengraph/router/Oauth.png",
//                        "GEOM",
//                        "OG.shape.router.Oauth",
//                        "Oauth2",
//                        "Succeeded,Failed",
//                        50, 50));
        shapesProvidedList.add(
                new WorkflowShapes(
                        "/service-console/resources/opengraph/router/Response.png",
                        "GEOM",
                        "OG.shape.router.Response",
                        "Response",
                        "Response",
                        50, 50));
        shapesProvidedList.add(
                new WorkflowShapes(
                        "/service-console/resources/opengraph/router/Function.svg",
                        "GEOM",
                        "OG.shape.router.Function",
                        "Function",
                        null,
                        50, 50));
        shapesProvidedList.add(
                new WorkflowShapes(
                        "/service-console/resources/opengraph/router/Api.png",
                        "GEOM",
                        "OG.shape.router.Api",
                        "Api",
                        "Response",
                        80, 50));
        shapesProvidedList.add(
                new WorkflowShapes(
                        "/service-console/resources/opengraph/router/Proxy.png",
                        "GEOM",
                        "OG.shape.router.Proxy",
                        "Proxy",
                        "Succeeded,Failed",
                        50, 50));
    }

    private String img;
    private String shapeType;
    private String shapeId;
    private String label;
    private String textLine;
    private int width;
    private int height;

    public WorkflowShapes(String img, String shapeType, String shapeId, String label, String textLine, int width, int height) {
        this.img = img;
        this.shapeType = shapeType;
        this.shapeId = shapeId;
        this.label = label;
        this.textLine = textLine;
        this.width = width;
        this.height = height;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getShapeType() {
        return shapeType;
    }

    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }

    public String getShapeId() {
        return shapeId;
    }

    public void setShapeId(String shapeId) {
        this.shapeId = shapeId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTextLine() {
        return textLine;
    }

    public void setTextLine(String textLine) {
        this.textLine = textLine;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
