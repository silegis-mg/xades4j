/*
 * XAdES4j - A Java library for generation and verification of XAdES signatures.
 * Copyright (C) 2012 Luis Goncalves.
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

import xades4j.UnsupportedAlgorithmException;
import xades4j.properties.BaseXAdESTimeStampProperty;
import xades4j.properties.QualifyingProperty;
import xades4j.properties.data.BaseXAdESTimeStampData;
import xades4j.providers.TimeStampTokenDigestException;
import xades4j.providers.TimeStampTokenSignatureException;
import xades4j.providers.TimeStampTokenStructureException;
import xades4j.providers.TimeStampTokenVerificationException;
import xades4j.providers.TimeStampVerificationData;
import xades4j.providers.TimeStampVerificationProvider;
import xades4j.utils.CannotAddDataToDigestInputException;
import xades4j.utils.TimeStampDigestInput;
import xades4j.utils.TimeStampDigestInputFactory;

/**
 *
 * @author Luís
 */
abstract class TimeStampVerifierBase<TData extends BaseXAdESTimeStampData> implements QualifyingPropertyVerifier<TData>
{

    private final TimeStampVerificationProvider tsVerifier;
    private final TimeStampDigestInputFactory tsInputFactory;
    private final String propName;

    public TimeStampVerifierBase(TimeStampVerificationProvider tsVerifier, TimeStampDigestInputFactory tsInputFactory, String propName)
    {
        this.tsVerifier = tsVerifier;
        this.tsInputFactory = tsInputFactory;
        this.propName = propName;
    }

    @Override
    public final QualifyingProperty verify(
            TData propData,
            Element elem,
            QualifyingPropertyVerificationContext ctx) throws InvalidPropertyException
    {
        try
        {
            TimeStampDigestInput digestInput = this.tsInputFactory.newTimeStampDigestInput(propData.getCanonicalizationAlgorithm());

            BaseXAdESTimeStampProperty prop = addPropSpecificTimeStampInputAndCreateProperty(
                    propData,
                    elem,
                    digestInput,
                    ctx);
            byte[] data = digestInput.getBytes();
            /**
             * Verify the time-stamp tokens on a time-stamp property data object. All
             * the tokens are verified, but the returned time-stamp is from the last token.
             */
            List<byte[]> tokens = propData.getTimeStampTokens();
            TimeStampVerificationData tsVerData = null;
            for (byte[] tkn : tokens)
            {
                tsVerData = this.tsVerifier.verifyToken(tkn, data, ctx);
            }

            prop.setTime(tsVerData.getTimeStampTokenTime());
            prop.setValidationData(tsVerData.getValidationData());

            // should be a noop, only ArchiveTimeStamp should use it to change the
            // verification time for all subsequent TimeStamps
            updateContextAfterVerification(prop,ctx);
            return prop;
        }
        catch(UnsupportedAlgorithmException ex)
        {
            throw getEx(ex, this.propName);
        }
        catch (CannotAddDataToDigestInputException ex)
        {
            throw new TimeStampDigestInputException(this.propName, ex);
        }
        catch (TimeStampTokenVerificationException ex)
        {
            throw getEx(ex, this.propName);
        }
        catch (Exception ex)
        {
            // Exceptions related to setTimeMethod.invoke(...)
            throw getEx(ex, this.propName);
        }
    }

    @Override
    public final QualifyingProperty verify(
            TData propData,
            QualifyingPropertyVerificationContext ctx) throws InvalidPropertyException
    {
        return verify(propData, null, ctx);
    }

    protected abstract BaseXAdESTimeStampProperty addPropSpecificTimeStampInputAndCreateProperty(
            TData propData,
            Element location,
            TimeStampDigestInput digestInput,
            QualifyingPropertyVerificationContext ctx) throws CannotAddDataToDigestInputException, TimeStampVerificationException;

    protected abstract void updateContextAfterVerification(
            QualifyingProperty prop,
            QualifyingPropertyVerificationContext ctx);

    private static TimeStampVerificationException getEx(
            final Exception ex,
            String propName)
    {
        if (ex instanceof TimeStampTokenDigestException)
        {
            return new TimeStampDigestMismatchException(propName);
        }

        if (ex instanceof TimeStampTokenSignatureException)
        {
            return new TimeStampInvalidSignatureException(propName, ex);
        }

        if (ex instanceof TimeStampTokenStructureException)
        {
            return new TimeStampInvalidTokenException(propName, ex);
        }

        return new TimeStampVerificationException(propName, ex)
        {
            private static final long serialVersionUID = 1L;

            @Override
            protected String getVerificationMessage()
            {
                return ex.getMessage();
            }
        };
    }
}
