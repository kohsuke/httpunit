package com.meterware.httpunit.dom;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;

/**
 * @author Kohsuke Kawaguchi
 */
public class DocumentBuilderFactoryFilter extends DocumentBuilderFactory {
    private DocumentBuilderFactory base;

    public DocumentBuilderFactoryFilter(DocumentBuilderFactory base) {
        this.base = base;
    }

    public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        return base.newDocumentBuilder();
    }

    public void setNamespaceAware(boolean awareness) {
        base.setNamespaceAware(awareness);
    }

    public void setValidating(boolean validating) {
        base.setValidating(validating);
    }

    public void setIgnoringElementContentWhitespace(boolean whitespace) {
        base.setIgnoringElementContentWhitespace(whitespace);
    }

    public void setExpandEntityReferences(boolean expandEntityRef) {
        base.setExpandEntityReferences(expandEntityRef);
    }

    public void setIgnoringComments(boolean ignoreComments) {
        base.setIgnoringComments(ignoreComments);
    }

    public void setCoalescing(boolean coalescing) {
        base.setCoalescing(coalescing);
    }

    public boolean isNamespaceAware() {
        return base.isNamespaceAware();
    }

    public boolean isValidating() {
        return base.isValidating();
    }

    public boolean isIgnoringElementContentWhitespace() {
        return base.isIgnoringElementContentWhitespace();
    }

    public boolean isExpandEntityReferences() {
        return base.isExpandEntityReferences();
    }

    public boolean isIgnoringComments() {
        return base.isIgnoringComments();
    }

    public boolean isCoalescing() {
        return base.isCoalescing();
    }

    public void setAttribute(String name, Object value) throws IllegalArgumentException {
        base.setAttribute(name, value);
    }

    public Object getAttribute(String name) throws IllegalArgumentException {
        return base.getAttribute(name);
    }

    public void setFeature(String name, boolean value) throws ParserConfigurationException {
        base.setFeature(name, value);
    }

    public boolean getFeature(String name) throws ParserConfigurationException {
        return base.getFeature(name);
    }

    public Schema getSchema() {
        return base.getSchema();
    }

    public void setSchema(Schema schema) {
        base.setSchema(schema);
    }

    public void setXIncludeAware(boolean state) {
        base.setXIncludeAware(state);
    }

    public boolean isXIncludeAware() {
        return base.isXIncludeAware();
    }
}
