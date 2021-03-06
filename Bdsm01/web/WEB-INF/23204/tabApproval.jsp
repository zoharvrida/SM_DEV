<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:url var="urlGrid" action="23204_list" namespace="/json" escapeAmp="false">
	<s:param name="IDBATCH"><%=request.getParameter("IDBATCH")%></s:param>
    <s:param name="mode" value="2" />
</s:url>
<s:actionerror name="actionError" />

<script type="text/javascript">
	Number.prototype.formatMoney = function(c, d, t) {
		var n = this;
		c = isNaN(c = Math.abs(c)) ? 2 : c;
		d = d == undefined ? "." : d;
		t = t == undefined ? "," : t;
		s = n < 0 ? "-" : "";
		i = parseInt(n = Math.abs(+n || 0).toFixed(c)) + "";
		j = (j = i.length) > 3 ? j % 3 : 0;
		return s + (j ? i.substr(0, j) + t : "")
				+ i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t)
				+ (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
	};
	
	function customCurrency(cellValue, options, rowObjects) {
		var result = cellValue;
		result = cellValue.replace(',', '.');
		result = parseFloat(result);

		return result.formatMoney(2);
	}
</script>

<div id="approvalGrid" style="overflow-x: auto; display: inline-block;" class=".ui-widget-header">
	<s:form id="frmApproval" action="23204_add" method="post">
		<s:hidden name="IDBATCH" value="" />
		<s:hidden name="recIds" value="" />
		<s:hidden name="isApprove" value=""/>
		<s:token />
	</s:form>
	<sj:a id="aApproval" formIds="frmApproval" targets="ph-main" cssClass="ui-helper-hidden" />
	
	<table>
		<tr>
			<td>Filename</td>
			<td>:</td>
			<td><s:label id="txtFilenameAprv" theme="css_xhtml" /></td>
		</tr>
	</table>
	
    <sjg:grid 
		id="aprvGridTable" 
        caption=""
        dataType="json" 
        href="%{urlGrid}"
        requestType="POST"
        pager="true"
        pagerPosition="right"
        gridModel="gridTemplate" 
        rowNum="15"
        rownumbers="true"
		shrinkToFit="false" 
        altRows="true" 
        navigator="true"
        navigatorAdd="false"
        navigatorDelete="false"
        navigatorEdit="false"
        navigatorRefresh="false"
        navigatorSearch="false"
		onGridCompleteTopics="aprvGridCompleted"
		multiselect="true"
        >
       	<sjg:gridColumn hidden="true" id="filename" name="filename" title="Filename" align="left" sortable="false" />
		<sjg:gridColumn name="tanggalBatch" title="Tanggal Batch" width="100" align="center" sortable="false" />
		<sjg:gridColumn name="totalRecord" title="Total Record to Process" width="100" align="left" sortable="false" formatter="integer"/>
		<sjg:gridColumn name="totalBatch" index="totalBatch" title="total Batch" width="100" formatter="integer" align="left" sortable="false" />
		<sjg:gridColumn name="totalAmount" title="Total Nominal" width="100" align="right" formatter="customCurrency" sortable="false" />
		</sjg:grid>
	<div id="divButtons" align="left">
    <table>
			<tr align="right">
				<td>
					<sj:submit
						id="btnApprove"
						formIds="frmApproval" 
						buttonIcon="ui-icon-gear" 
						button="true"
						key="button.approve" 
						disabled="false" 
						targets="ph-main"
						onBeforeTopics="btnApprove_beforeSubmit" 
					/>
        </td>
            </tr>
    </table>
	</div>
</div>

<script type="text/javascript">
	jQuery(document).ready(function() {
		$("#aprvGridTable").unsubscribe("aprvGridCompleted");
		$("#aprvGridTable").subscribe("aprvGridCompleted", function(event, data) {
			if ($("#aprvGridTable_pager_left #note").length == 0) {
				$("#divButtons").width($("#aprvGridTable").width());

				if ($('#aprvGridTable').jqGrid('getGridParam', 'records') == 0) {
					$("#aprvGridTable_pager_left").append("<span id='note' style='color:red'>No records found</span>");
					$("#btnApprove").attr("style", "display: none");
					$("#btnReject").attr("style", "display: none");
				}
				else {
					$("th.ui-th-column div").css({"white-space": "normal", "height" : "auto"});
					$("input.cbox").css("vertical-align", "middle");
					//$("td[role='gridcell']").css("vertical-align", "middle");
                    $("#txtFilenameAprv").text($(data).jqGrid("getCell", 1, 2));
					$("#btnApprove").attr("style", "display: block");
					$("#btnReject").attr("style", "display: block");					
				}
			}
		});
		
		$("#approvalGrid").width(screen.availWidth - 425);

		var f_beforeSubmitAprove = function(event) {
			var arrSelected = $("#aprvGridTable").jqGrid("getGridParam", "selarrrow");
			var arrRecId = [];
			event.originalEvent.options.submit = false;
			 /* convert to string */
			 for (var i=0; i<arrSelected.length; i++)
					arrRecId[i] = $('#aprvGridTable').jqGrid('getCell', arrSelected[i], "totalRecord");

				arrRecId.splice(0, 0);
				$("#frmApproval_IDBATCH").val($("#gRPID").val());
				$("#frmApproval_recIds").val("T");
				$("#frmApproval_isApprove").val("true");
				$("#btnApprove").button("disable");
				//alert("Approve");
				$("#aApproval").click();
		};
		
		var f_beforeSubmitReject = function(event) {
			var arrSelected = $("#aprvGridTable").jqGrid("getGridParam", "selarrrow");
			var arrRecId = [];
			event.originalEvent.options.submit = false;

			if (arrSelected.length == 0)
				alert("Please select at least one!!!");
			else {
				for (var i=0; i<arrSelected.length; i++)
					arrRecId[i] = $('#aprvGridTable').jqGrid('getCell', arrSelected[i], "totalRecord");

				arrRecId.splice(0, 0); /* convert to string */
				$("#frmApproval_IDBATCH").val($("#gRPID").val());
				$("#frmApproval_recIds").val(arrRecId);
				$("#frmApproval_isApprove").val("false");
				$("#btnReject").button("disable");
				//alert("Reject");
				$("#aApproval").click();
            }
		};
		
		$("#btnApprove")
			.unsubscribe("btnApprove_beforeSubmit")
			.subscribe("btnApprove_beforeSubmit", f_beforeSubmitAprove);
       
		/*$("#btnReject")
			.unsubscribe("btnReject_beforeSubmit")
			.subscribe("btnReject_beforeSubmit", f_beforeSubmitReject);*/
        });
</script>
