<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:set var="master_" scope="request">${param.master}</s:set>
<s:set var="constant_" scope="request">${param.constant}</s:set>
<s:set var="hideId_" scope="request">${param.hideId}</s:set>
<s:url var="urlGrid" action="DataMaster_list" namespace="/json" />

<sjg:grid 
	id="gridTable"
	caption=""
	dataType="local"
	href="%{urlGrid}"
	requestType="POST"
	pager="true"
	pagerPosition="right"
	gridModel="gridTemplate"
	rowNum="15"
	rownumbers="true"
	altRows="true"
	autowidth="true"
	navigator="false"
	navigatorAdd="false"
	navigatorDelete="false"
	navigatorEdit="false"
	navigatorRefresh="false"
	navigatorSearch="false"
	onSelectRowTopics="selectRow"
	onGridCompleteTopics="gridCompleted"
	>
		<s:if test='%{#request.hideId_=="true"}'>
			<sjg:gridColumn 
				name="id"
				title="ID"
				hidden="true"
			/>
		</s:if>
		<s:else>
			<sjg:gridColumn 
				name="id"
				index="id"
				title="ID"
				width="30"
				align="center"
				sortable="false"
			/>
		</s:else>
		
		<sjg:gridColumn
			name="desc"
			index="desc"
			title="Description"
			sortable="false"
		/>
</sjg:grid>

<script type="text/javascript">
	$(function() {
		$("#gridTable").unsubscribe("selectRow");
		$("#gridTable").subscribe("selectRow",
				function(event, data) {
					$("#" + dlgParams.id).val($(data).jqGrid("getCell", event.originalEvent.id, "id"));
					$("#" + dlgParams.desc).val($(data).jqGrid("getCell", event.originalEvent.id, "desc"));
					if (dlgParams.onClose != undefined) {
						dlgParams.onClose();
					}
					$("#ph-dlg").dialog("close");
				});
		
		$("#gridTable").unsubscribe("gridCompleted");
		$("#gridTable").subscribe("gridCompleted",
				function(event, data) {
					$("#ph-dlg").dialog({
						width : Math.min(screen.availWidth - 20, 400),
						height : $("#gbox_gridTable").height() + 
									$("div.ui-dialog-titlebar.ui-widget-header").height() + 44
					});
					$("#ph-dlg").dialog({
						position : "center"
					});
					$(data).jqGrid("setGridWidth", Math.min(screen.availWidth - 20, 400) - 22);
				});
				
		$("#gridTable").jqGrid('setGridParam', {
				datatype: 'json',
				postData: {
					'master': function() {return '<s:property value="%{#request.master_}" />';},
					'constant': function() {return '<s:property value="%{#request.constant_}" />';}
				}
			});
			$("#gridTable").trigger('reloadGrid');
	});
</script>
