<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags"%>
<script type="text/javascript">
    $(function() {
        if (tmpParams.onClose != undefined) {
            tmpParams.onClose('<s:property value="%{ratUsdIdr}" />');
        }
    });
</script>