/*
 * Copyright (C) 2015 Bahamas Project (http://www.opencloudengine.org).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudengine.garuda.jaxb.opengraph;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="cell">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="shapeType" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;enumeration value="GEOM"/>
 *                       &lt;enumeration value="TEXT"/>
 *                       &lt;enumeration value="HTML"/>
 *                       &lt;enumeration value="IMAGE"/>
 *                       &lt;enumeration value="EDGE"/>
 *                       &lt;enumeration value="GROUP"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="shapeId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="x" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                 &lt;attribute name="y" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                 &lt;attribute name="width" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                 &lt;attribute name="height" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                 &lt;attribute name="geom" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="parent" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="fromEdge" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="toEdge" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="from" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="to" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="fromLabel" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="toLabel" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="style" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="angle" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                 &lt;attribute name="data" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="width" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="height" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="data" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "cell"
})
@XmlRootElement(name = "opengraph")
public class Opengraph {

    protected List<Cell> cell;
    @XmlAttribute(name = "width", required = true)
    protected int width;
    @XmlAttribute(name = "height", required = true)
    protected int height;
    @XmlAttribute(name = "data")
    protected String data;

    /**
     * Gets the value of the cell property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cell property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCell().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link org.opencloudengine.garuda.jaxb.opengraph.Opengraph.Cell }
     */
    public List<Cell> getCell() {
        if (cell == null) {
            cell = new ArrayList<Cell>();
        }
        return this.cell;
    }

    /**
     * Gets the value of the width property.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the value of the width property.
     */
    public void setWidth(int value) {
        this.width = value;
    }

    /**
     * Gets the value of the height property.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the value of the height property.
     */
    public void setHeight(int value) {
        this.height = value;
    }

    /**
     * Gets the value of the data property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getData() {
        return data;
    }

    /**
     * Sets the value of the data property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setData(String value) {
        this.data = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * <p/>
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p/>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="shapeType" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;enumeration value="GEOM"/>
     *             &lt;enumeration value="TEXT"/>
     *             &lt;enumeration value="HTML"/>
     *             &lt;enumeration value="IMAGE"/>
     *             &lt;enumeration value="EDGE"/>
     *             &lt;enumeration value="GROUP"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="shapeId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="x" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
     *       &lt;attribute name="y" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
     *       &lt;attribute name="width" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
     *       &lt;attribute name="height" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
     *       &lt;attribute name="geom" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="parent" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="fromEdge" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="toEdge" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="from" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="to" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="fromLabel" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="toLabel" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="style" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="angle" type="{http://www.w3.org/2001/XMLSchema}int" />
     *       &lt;attribute name="data" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Cell {

        @XmlAttribute(name = "id", required = true)
        protected String id;
        @XmlAttribute(name = "shapeType", required = true)
        protected String shapeType;
        @XmlAttribute(name = "shapeId", required = true)
        protected String shapeId;
        @XmlAttribute(name = "x", required = true)
        protected int x;
        @XmlAttribute(name = "y", required = true)
        protected int y;
        @XmlAttribute(name = "width", required = true)
        protected int width;
        @XmlAttribute(name = "height", required = true)
        protected int height;
        @XmlAttribute(name = "geom", required = true)
        @XmlSchemaType(name = "anySimpleType")
        protected String geom;
        @XmlAttribute(name = "parent")
        protected String parent;
        @XmlAttribute(name = "fromEdge")
        protected String fromEdge;
        @XmlAttribute(name = "toEdge")
        protected String toEdge;
        @XmlAttribute(name = "from")
        protected String from;
        @XmlAttribute(name = "to")
        protected String to;
        @XmlAttribute(name = "label")
        protected String label;
        @XmlAttribute(name = "fromLabel")
        protected String fromLabel;
        @XmlAttribute(name = "toLabel")
        protected String toLabel;
        @XmlAttribute(name = "value")
        protected String value;
        @XmlAttribute(name = "style")
        protected String style;
        @XmlAttribute(name = "angle")
        protected Integer angle;
        @XmlAttribute(name = "data")
        protected String data;

        /**
         * Gets the value of the id property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getId() {
            return id;
        }

        /**
         * Sets the value of the id property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setId(String value) {
            this.id = value;
        }

        /**
         * Gets the value of the shapeType property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getShapeType() {
            return shapeType;
        }

        /**
         * Sets the value of the shapeType property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setShapeType(String value) {
            this.shapeType = value;
        }

        /**
         * Gets the value of the shapeId property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getShapeId() {
            return shapeId;
        }

        /**
         * Sets the value of the shapeId property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setShapeId(String value) {
            this.shapeId = value;
        }

        /**
         * Gets the value of the x property.
         */
        public int getX() {
            return x;
        }

        /**
         * Sets the value of the x property.
         */
        public void setX(int value) {
            this.x = value;
        }

        /**
         * Gets the value of the y property.
         */
        public int getY() {
            return y;
        }

        /**
         * Sets the value of the y property.
         */
        public void setY(int value) {
            this.y = value;
        }

        /**
         * Gets the value of the width property.
         */
        public int getWidth() {
            return width;
        }

        /**
         * Sets the value of the width property.
         */
        public void setWidth(int value) {
            this.width = value;
        }

        /**
         * Gets the value of the height property.
         */
        public int getHeight() {
            return height;
        }

        /**
         * Sets the value of the height property.
         */
        public void setHeight(int value) {
            this.height = value;
        }

        /**
         * Gets the value of the geom property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getGeom() {
            return geom;
        }

        /**
         * Sets the value of the geom property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setGeom(String value) {
            this.geom = value;
        }

        /**
         * Gets the value of the parent property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getParent() {
            return parent;
        }

        /**
         * Sets the value of the parent property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setParent(String value) {
            this.parent = value;
        }

        /**
         * Gets the value of the fromEdge property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getFromEdge() {
            return fromEdge;
        }

        /**
         * Sets the value of the fromEdge property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setFromEdge(String value) {
            this.fromEdge = value;
        }

        /**
         * Gets the value of the toEdge property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getToEdge() {
            return toEdge;
        }

        /**
         * Sets the value of the toEdge property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setToEdge(String value) {
            this.toEdge = value;
        }

        /**
         * Gets the value of the from property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getFrom() {
            return from;
        }

        /**
         * Sets the value of the from property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setFrom(String value) {
            this.from = value;
        }

        /**
         * Gets the value of the to property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getTo() {
            return to;
        }

        /**
         * Sets the value of the to property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setTo(String value) {
            this.to = value;
        }

        /**
         * Gets the value of the label property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getLabel() {
            return label;
        }

        /**
         * Sets the value of the label property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setLabel(String value) {
            this.label = value;
        }

        /**
         * Gets the value of the fromLabel property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getFromLabel() {
            return fromLabel;
        }

        /**
         * Sets the value of the fromLabel property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setFromLabel(String value) {
            this.fromLabel = value;
        }

        /**
         * Gets the value of the toLabel property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getToLabel() {
            return toLabel;
        }

        /**
         * Sets the value of the toLabel property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setToLabel(String value) {
            this.toLabel = value;
        }

        /**
         * Gets the value of the value property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the style property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getStyle() {
            return style;
        }

        /**
         * Sets the value of the style property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setStyle(String value) {
            this.style = value;
        }

        /**
         * Gets the value of the angle property.
         *
         * @return possible object is
         * {@link Integer }
         */
        public Integer getAngle() {
            return angle;
        }

        /**
         * Sets the value of the angle property.
         *
         * @param value allowed object is
         *              {@link Integer }
         */
        public void setAngle(Integer value) {
            this.angle = value;
        }

        /**
         * Gets the value of the data property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getData() {
            return data;
        }

        /**
         * Sets the value of the data property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setData(String value) {
            this.data = value;
        }

    }

}
