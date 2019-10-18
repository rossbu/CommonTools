package com.tools.rdf;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;




/**
 * text like urn:company:xxx:yyy
 *
 * @author tbu
 *
 */
public class SimpleRDFTree  implements Serializable{


    /**
     *
     */
    private static final long serialVersionUID = 8655050214570182286L;
    private Object id;
    private String text;
    private Object value;
    private Boolean complete;
    private Boolean isexpand;
    private Object checkstate;
    private Boolean hasChildren;
    private Boolean showcheck;
    private List<SimpleRDFTree> childnodes;


    public SimpleRDFTree(Object id, String text, Object value, Boolean complete,
                   Boolean isexpand, Object checkstate, Boolean hasChildren,
                   List<SimpleRDFTree> childNodes,Boolean showcheck, List<SimpleRDFTree> childnodes) {
        super();
        this.id = id;
        this.text = text;
        this.value = value;
        this.complete = complete;
        this.isexpand = isexpand;
        this.checkstate = checkstate;
        this.hasChildren = hasChildren;
        this.showcheck = showcheck;
        this.childnodes = childnodes;
    }

    public SimpleRDFTree() {
    }


    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public Boolean getIsexpand() {
        return isexpand;
    }

    public void setIsexpand(Boolean isexpand) {
        this.isexpand = isexpand;
    }

    public Object getCheckstate() {
        return checkstate;
    }

    public void setCheckstate(Object checkstate) {
        this.checkstate = checkstate;
    }

    public Boolean getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(Boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public List<SimpleRDFTree> getChildnodes() {
        return childnodes;
    }

    public void setChildnodes(List<SimpleRDFTree> childnodes) {
        this.childnodes = childnodes;
    }

    public Boolean getShowcheck() {
        return showcheck;
    }

    public void setShowcheck(Boolean showcheck) {
        this.showcheck = showcheck;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("text", text)
                .append("value", value).append("complete", complete).append(
                        "isexpand", isexpand).append("checkstate", checkstate)
                .append("hasChildren", hasChildren).append("showcheck",
                        showcheck).append("childnodes", childnodes).toString();
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof SimpleRDFTree))
            return false;
        SimpleRDFTree castOther = (SimpleRDFTree) other;
        return new EqualsBuilder().append(text, castOther.text).append(value,
                castOther.value).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(text).append(value).toHashCode();
    }
}
