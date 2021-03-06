/*
 * XAdES4j - A Java library for generation and verification of XAdES signatures.
 * Copyright (C) 2010 Luis Goncalves.
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
package xades4j.verification;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import xades4j.properties.data.AllDataObjsTimeStampData;
import xades4j.properties.data.ArchiveTimeStampData;
import xades4j.properties.data.AttrAuthoritiesCertValuesData;
import xades4j.properties.data.AttributeRevocationValuesData;
import xades4j.properties.data.CertificateValuesData;
import xades4j.properties.data.CompleteCertificateRefsData;
import xades4j.properties.data.CompleteRevocationRefsData;
import xades4j.properties.data.GenericDOMData;
import xades4j.properties.data.IndividualDataObjsTimeStampData;
import xades4j.properties.data.OtherPropertyData;
import xades4j.properties.data.RevocationValuesData;
import xades4j.properties.data.SigAndRefsTimeStampData;
import xades4j.properties.data.SignaturePolicyData;
import xades4j.properties.data.SignatureTimeStampData;
import xades4j.properties.data.SignerRoleData;
import xades4j.properties.data.TimeStampValidationDataData;
import xades4j.utils.PropertiesSet;
import xades4j.properties.data.CommitmentTypeData;
import xades4j.properties.data.DataObjectFormatData;
import xades4j.properties.data.PropertyDataObject;
import xades4j.xml.unmarshalling.QualifyingPropertiesDataCollector;
import xades4j.properties.data.SignatureProdPlaceData;
import xades4j.properties.data.SigningCertificateData;
import xades4j.properties.data.SigningTimeData;

/**
 *
 * @author Luís
 */
class QualifPropsDataCollectorImpl implements QualifyingPropertiesDataCollector
{
    private final PropertiesSet<PropertyDataObject> propsData;

    public QualifPropsDataCollectorImpl()
    {
        propsData = new PropertiesSet<PropertyDataObject>(1);
    }

    @Override
    public void setSigningTime(SigningTimeData sigTimeData)
    {
        propsData.put(sigTimeData);
    }

    @Override
    public void setSignerRole(SignerRoleData signerRoleData)
    {
        propsData.put(signerRoleData);
    }

    @Override
    public void setSignatureProdPlace(SignatureProdPlaceData sigProdPlaceData)
    {
        propsData.put(sigProdPlaceData);
    }

    @Override
    public void setSigningCertificate(SigningCertificateData signingCertData)
    {
        propsData.put(signingCertData);
    }

    @Override
    public void setSignaturePolicy(SignaturePolicyData sigPolicyData)
    {
        propsData.put(sigPolicyData);
    }

    @Override
    public void setCompleteCertificateRefs(
            CompleteCertificateRefsData completeCertRefsData)
    {
        propsData.put(completeCertRefsData);
    }

    @Override
    public void setCompleteRevocRefs(
            CompleteRevocationRefsData completeRecovRefsData)
    {
        propsData.put(completeRecovRefsData);
    }

    @Override
    public void addSignatureTimeStamp(SignatureTimeStampData sigTSData)
    {
        propsData.add(sigTSData);
    }

    @Override
    public void addSigAndRefsTimeStamp(SigAndRefsTimeStampData tsData)
    {
        propsData.add(tsData);
    }

    @Override
    public void setCertificateValues(CertificateValuesData certificateValuesData)
    {
        propsData.add(certificateValuesData);
    }

    @Override
    public void setAttrAuthoritiesCertValues(AttrAuthoritiesCertValuesData attrAuthoritiesCertValuesData)
    {
        propsData.add(attrAuthoritiesCertValuesData);
    }

    @Override
    public void setRevocationValues(RevocationValuesData revocationValuesData)
    {
        propsData.add(revocationValuesData);
    }

    @Override
    public void setAttributeRevocationValues(AttributeRevocationValuesData attrRevocValData)
    {
        propsData.add(attrRevocValData);
    }

    @Override
    public void addCommitmentType(CommitmentTypeData commitmentData)
    {
        propsData.add(commitmentData);
    }

    @Override
    public void addDataObjectFormat(DataObjectFormatData formatData)
    {
        propsData.add(formatData);
    }

    @Override
    public void addAllDataObjsTimeStamp(AllDataObjsTimeStampData objsTSData)
    {
        propsData.add(objsTSData);
    }

    @Override
    public void addIndividualDataObjsTimeStamp(
            IndividualDataObjsTimeStampData objsTSData)
    {
        propsData.add(objsTSData);
    }

    @Override
    public void addGenericDOMData(GenericDOMData domData)
    {
        propsData.add(domData);
    }

    @Override
    public void addOther(OtherPropertyData otherData)
    {
        propsData.add(otherData);
    }

    /**/
    public List<PropertyDataObject> getPropertiesData()
    {
        return propsData.getProperties();
    }

    @Override
    public void linkPropertyToElem(Element node)
    {
        //noop
    }

    @Override
    public Node getPropertyNode(PropertyDataObject pdo)
    {
        throw new UnsupportedOperationException("QualifPropsDataCollectorImpl does not"+
                    " support getPropertyNode() method. Use HybridQualifPropsDataCollectorImpl.");
    }

    @Override
    public void addArchiveTimeStamp(ArchiveTimeStampData tsData)
    {
        propsData.add(tsData);
    }

    @Override
    public void addTimeStampValidationDataData(
            TimeStampValidationDataData timeStampValidationDataData)
    {
        propsData.add(timeStampValidationDataData);
    }
}
