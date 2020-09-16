package project.bridgetek.com.applib.main.bean.workbench;

import java.util.List;

/**
 * Created by czz on 19-5-6.
 */

public class TreeNode {

    /**
     * childNodes : [{"childNodeId":"53298b7a-404c-4337-aa7f-80b2a4ca6681","childNodeCode":"GT","childNodeName":"钢铁股份公司","childNodeType":"Company"},{"childNodeId":"207fa1a9-160c-4943-a89b-8fa4db0547ce","childNodeCode":"NR","childNodeName":"能源股份公司","childNodeType":"Company"},{"childNodeId":"d3d1ca6d-48ae-46fa-a86b-04f3e062f3b1","childNodeCode":"XCGF","childNodeName":"西昌钢钒","childNodeType":"Plant"}]
     * parentCode : #
     */

    private String parentCode;
    private List<ChildNodesBean> childNodes;

    public TreeNode() {
    }

    public TreeNode(String parentCode, List<ChildNodesBean> childNodes) {
        this.parentCode = parentCode;
        this.childNodes = childNodes;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public List<ChildNodesBean> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<ChildNodesBean> childNodes) {
        this.childNodes = childNodes;
    }

    public static class ChildNodesBean {
        /**
         * childNodeId : 53298b7a-404c-4337-aa7f-80b2a4ca6681
         * childNodeCode : GT
         * childNodeName : 钢铁股份公司
         * childNodeType : Company
         */

        private String childNodeId;
        private String childNodeCode;
        private String childNodeName;
        private String childNodeType;
        private boolean selection = false;

        public ChildNodesBean() {
        }

        public ChildNodesBean(String childNodeId, String childNodeCode, String childNodeName, String childNodeType, boolean selection) {
            this.childNodeId = childNodeId;
            this.childNodeCode = childNodeCode;
            this.childNodeName = childNodeName;
            this.childNodeType = childNodeType;
            this.selection = selection;
        }

        public boolean isSelection() {
            return selection;
        }

        public void setSelection(boolean selection) {
            this.selection = selection;
        }

        public String getChildNodeId() {
            return childNodeId;
        }

        public void setChildNodeId(String childNodeId) {
            this.childNodeId = childNodeId;
        }

        public String getChildNodeCode() {
            return childNodeCode;
        }

        public void setChildNodeCode(String childNodeCode) {
            this.childNodeCode = childNodeCode;
        }

        public String getChildNodeName() {
            return childNodeName;
        }

        public void setChildNodeName(String childNodeName) {
            this.childNodeName = childNodeName;
        }

        public String getChildNodeType() {
            return childNodeType;
        }

        public void setChildNodeType(String childNodeType) {
            this.childNodeType = childNodeType;
        }
    }
}
