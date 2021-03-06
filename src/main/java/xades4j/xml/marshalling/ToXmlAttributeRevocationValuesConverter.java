/*
 * XAdES4j - A Java library for generation and verification of XAdES signatures.
 * Copyright (C) 2012 Hubert Kario - QBS.
 *
 * XAdES4j is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or any later version.
 *
 * XAdES4j is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with XAdES4j. If not, see <http://www.gnu.org/licenses/>.
 */
package xades4j.xml.marshalling;

import java.util.Collection;
import java.util.List;

import org.w3c.dom.Document;

import xades4j.properties.data.AttributeRevocationValuesData;
import xades4j.properties.data.PropertyDataObject;
import xades4j.xml.bind.xades.XmlCRLValuesType;
import xades4j.xml.bind.xades.XmlEncapsulatedPKIDataType;
import xades4j.xml.bind.xades.XmlRevocationValuesType;
import xades4j.xml.bind.xades.XmlUnsignedPropertiesType;

public class ToXmlAttributeRevocationValuesConverter
        implements UnsignedPropertyDataToXmlConverter
{
    @Override
    public void convertIntoObjectTree(PropertyDataObject propData,
            XmlUnsignedPropertiesType xmlProps, Document doc)
    {
        Collection<byte[]> crlValues = ((AttributeRevocationValuesData)propData).getData();

        XmlRevocationValuesType xmlAttrRevocValues = new XmlRevocationValuesType();
        XmlCRLValuesType xmlCRLValues = new XmlCRLValuesType();
        xmlAttrRevocValues.setCRLValues(xmlCRLValues);

        List<XmlEncapsulatedPKIDataType> xmlCRLs = xmlCRLValues.getEncapsulatedCRLValue();

        for (byte[] encodCrl : crlValues)
        {
            XmlEncapsulatedPKIDataType xmlEncodedCrl = new XmlEncapsulatedPKIDataType();
            xmlEncodedCrl.setValue(encodCrl);
            xmlCRLs.add(xmlEncodedCrl);
        }

        xmlProps.getUnsignedSignatureProperties()
                .setAttributeRevocationValues(xmlAttrRevocValues);
    }
}
