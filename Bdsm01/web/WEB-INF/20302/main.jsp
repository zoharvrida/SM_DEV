<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="sj" uri="/struts-jquery-tags" %>


<% if (!request.getMethod().equals("GET")) { %>
    <s:url var="ajaxUpdateTitle" action="20302_title" />
    <s:url var="ajaxUpdateButtons" action="20302_buttons" />
    
    <sj:a id="tempTitle" href="%{ajaxUpdateTitle}" targets="ph-title" cssClass="ui-helper-hidden"></sj:a>
    <sj:a id="tempButtons" href="%{ajaxUpdateButtons}" targets="ph-buttons" cssClass="ui-helper-hidden"></sj:a>
    
    <s:form id="tempFrmDlgMfcUd" action="dlg">
        <s:hidden name="dialog" value="dlgMfcUd" />
        <s:hidden name="noCif" />
        <s:hidden name="noUd" />
        <s:token />
    </s:form>
    <sj:a id="tempDlgMfcUd" formIds="tempFrmDlgMfcUd" targets="ph-dlg" cssClass="ui-helper-hidden"></sj:a>
    
    <s:form id="formCifInfo" action="20302_inquiry.action">
        <s:hidden name="noCif" />
        <s:token />
    </s:form>
    <sj:a id="tempCifInfo" formIds="formCifInfo" targets="ph-temp" cssClass="ui-helper-hidden"></sj:a>
    
    <s:form id="frmDlgTypeUD" action="dlg">
        <s:hidden name="dialog" value="dlgValasMonitoringTypeUD" />
        <s:hidden name="master" value="UnderlyingDocumentType" />
        <s:hidden name="constant" value="true" />
        <s:token />
    </s:form>
    <sj:a id="aDlgTypeUD" formIds="frmDlgTypeUD" targets="ph-dlg" cssClass="ui-helper-hidden" />
    
    <s:form id="formGetRatUsdIdr" action="20302_inqRatUsdIdr.action">
        <s:token />
    </s:form>
    <sj:a id="tempGetRatUsdIdr" formIds="formGetRatUsdIdr" targets="ph-temp" cssClass="ui-helper-hidden"></sj:a>
        
    <s:form id="formGetRatFcyIdr" action="20302_inqRatFcyIdr.action">
        <s:hidden name="ccyUd" />
        <s:token />
    </s:form>
    <sj:a id="tempGetRatFcyIdr" formIds="formGetRatFcyIdr" targets="ph-temp" cssClass="ui-helper-hidden"></sj:a>
        
    <s:form id="tempFrmDlgAuth" action="dlg">
        <s:hidden name="dialog" value="dlgAuth" />
        <s:hidden name="idMenu" value="20302" />
        <s:token />
    </s:form>
    <sj:a id="tempDlgAuth" formIds="tempFrmDlgAuth" targets="ph-dlg" cssClass="ui-helper-hidden" onSuccessTopics="dlgAuthOpenedSuccess"></sj:a>
    
    <s:form id="frmAutocompleter">
        <s:hidden name="page" value="1" />
        <s:hidden name="rows" value="10" />
    </s:form>
    
    <s:form id="formMessage" action="20302_retrieveRateMessage.action">
        <s:hidden name="tagMessage" value="TXT_CLS_RATE" />
        <s:token />
    </s:form>
    <sj:a id="tempformMessage" formIds="formMessage" targets="ph-temp" cssClass="ui-helper-hidden"></sj:a>
    
    <s:actionmessage id="actionMessage"/>
    <s:actionerror name="actionError"/>
    
    <s:form id="frmMain" name="frmMain" action="20302" focusElement="frmMain_noCif" validate="true">
        <s:textfield
            name="noCif"
            requiredLabel="true"
            size="10"
            maxlength="10"
            cssClass="ui-widget ui-widget-content"
            key="label.noCif"
            disabled="true"
            />
        <s:textfield
            name="namCustFull"
            size="50"
            maxlength="50"
            cssClass="ui-widget ui-widget-content"
            label=""
            labelSeparator=""
            disabled="true"
            />
        <s:textfield
            name="noUdSearch"
            size="45"
            maxlength="50"
            cssClass="ui-widget ui-widget-content"
            key="label.noUdSearch"
            disabled="true"
            />
        <sj:a
            id="btnFindUd"
            button="true"
            disabled="true"
            >...</sj:a>
        <s:textfield
            name="noUd"
            requiredLabel="true"
            size="45"
            maxlength="50"
            cssClass="ui-widget ui-widget-content"
            key="label.noUd"
            disabled="true"
            /> <s:hidden name="noUdIsExist" value="false" disabled="true" />
        <s:textfield
            name="txtTypeUD"
            requiredLabel="true"
            size="40"
            cssClass="ui-widget ui-widget-content"
            key="label.valas.monitoring.typeUD"
            /> <s:hidden name="typeUD" />
        <sj:a
            id="btnTypeUDLookup"
            button="true"
            disabled="true"
            >...</sj:a>
        <s:textfield
            name="payeeName"
            requiredLabel="true"
            size="63"
            maxlength="120"
            cssClass="ui-widget ui-widget-content"
            key="label.valas.monitoring.payeeName"
            />
        <s:label 
            key="label.valas.monitoring.payeeCountry"
            requiredLabel="true" 
            />
        <s:url var="urlCountryLookup" action="DataMaster_countryList" namespace="json" escapeAmp="false" />
        <sj:autocompleter
            id="payeeCountry"
            name="payeeCountry"
            href="%{urlCountryLookup}"
            list="gridTemplate"
            listKey="id"
            listValue="desc"
            delay="50"
            loadMinimumCount="2"
            formIds="frmAutocompleter"
            size="63"
            onblur="if($('#payeeCountry').val() == '') {$('#payeeCountry_widget').attr('value', '');}"
            onchange="$('#payeeCountry').val('');"
            />
        <s:textfield
            name="tradingProduct"
            requiredLabel="true"
            size="63"
            maxlength="120"
            cssClass="ui-widget ui-widget-content"
            key="label.valas.monitoring.tradingProduct"
            />
        <sj:datepicker
            id="dtIssued"
            name="dtIssued"
            key="label.valas.monitoring.dtIssued"
            cssClass="ui-widget ui-widget-content"        
            displayFormat="dd/mm/yy"
            maxDate="%{new java.util.Date(#session.dtBusiness.getTime())}"
            buttonImageOnly="true"
            readOnly="true"
            />
        <sj:datepicker
            id="dtExpiry"
            name="dtExpiry"
            key="label.dtExpiry"
            cssClass="ui-widget ui-widget-content"        
            displayFormat="dd/mm/yy"
            minDate="%{new java.util.Date(#session.dtBusiness.getTime())}"
            buttonImageOnly="true"
            readOnly="true"
            />
        <s:url id="getListCcyCode" action="json/20302_getListCcyCode"/>
        <sj:select
            id="ccyUd"
            name="ccyUd"
            href="%{getListCcyCode}"
            list="listCcy"
            cssClass="ui-widget ui-widget-content"
            key="label.ccyUd"
            emptyOption="true"
            headerKey="-1"
            />
        <s:textfield
            name="amtLimitScr"
            requiredLabel="true"
            size="15"
            maxlength="15"
            cssClass="ui-widget ui-widget-content"
            key="label.amtLimit"
            disabled="true"
            />
        <s:hidden name="amtLimit" />
        <s:textfield
            name="ratFcyIdrScr"
            requiredLabel="true"
            size="15"
            maxlength="15"
            cssClass="ui-widget ui-widget-content"
            key="label.ratFcyIdr"
            disabled="true"
            />    
        <s:hidden name="ratFcyIdr" />
        <s:textfield
            name="amtLimitScrUSD"
            size="15"
            maxlength="15"
            cssClass="ui-widget ui-widget-content"
            key="label.amtLimitUSD"
            disabled="true"
            />
        <s:hidden name="amtLimitUSD" /> 
        <s:textfield
            name="usdIdrRateScr"
            size="15"
            maxlength="15"
            cssClass="ui-widget ui-widget-content"
            key="label.usdIdrRate"
            disabled="true"
            />
        <s:hidden name="usdIdrRate" />         
       
        <s:textarea
            name="remarks"
            cssClass="ui-widget ui-widget-content"
            disabled="true"
            key="label.remarks"
            cols="50" rows="3"
            maxlength="600"
            />
        
        <s:textfield
            name="cdBranch"
            size="5"
            maxlength="5"
            cssClass="ui-widget ui-widget-content"
            key="label.cdBranch"
            disabled="true"
            />
        <s:textfield
            name="status"
            size="7"
            maxlength="7"
            cssClass="ui-widget ui-widget-content"
            key="label.status"
            disabled="true"
            />
        <s:hidden name="idMaintainedBy" value="%{#session.idUser}" />
        <s:hidden name="idMaintainedSpv" />
        <sj:submit
            id="btnSave"
            formIds="frmMain"
            buttonIcon="ui-icon-gear"
            button="true"
            key="button.save"
            disabled="true"
            targets="ph-main"
            onBeforeTopics="beforeSubmit"
            />
        <s:token />
    </s:form>    
    <div id="divMessage"></div>
    
    
    <script type="text/javascript">
        function calcRatUsd(ccy) {
            tmpParams = {};
            tmpParams.onClose = function(ratUsdIdr) {
                //if (ccy != "USD")
                    $("#frmMain_usdIdrRate").val(ratUsdIdr);
                //else
                //    $("#frmMain_usdIdrRate").val(1);
                $("#frmMain_usdIdrRateScr").val($("#frmMain_usdIdrRate").val());
                $("#frmMain_usdIdrRateScr").formatCurrency({decimalSymbol: '.', digitGroupSymbol: ',', symbol: ''});
            };
            $("#tempGetRatUsdIdr").click();
        }
        
        function calcRatFcy(ccy) {
            tmpParamsCcy = {};
            tmpParamsCcy.onClose = function(ratFcyIdr) {
                $("#frmMain_ratFcyIdr").val(ratFcyIdr);
                $("#frmMain_ratFcyIdrScr").val($("#frmMain_ratFcyIdr").val());
                $("#frmMain_ratFcyIdrScr").formatCurrency({decimalSymbol: '.', digitGroupSymbol: ',', symbol: ''});            
            };
            $("#formGetRatFcyIdr_ccyUd").val(ccy);
            $("#tempGetRatFcyIdr").click();
        }
        
        function messageBox(type, message, onCloseFunction) {
            var title = "";
            var icon = "";
            
            if (type == 1) {
                title = "Warning";
                icon = "ui-icon-alert";
            }
            else if (type == 2) {
                title = "Error";
                icon = "ui-icon-circle-close";
            }
            else if (type == 3) {
                title= "Information";
                icon = "ui-icon-info";
            }
            
            var $current = $("#divMessage");
            var $clone = $current.clone();
            var $parent = $current.parent();
            var $prevSibling = $current.prev();
            
            if ($current.length) {
                $current
                .html("<p style='text-align: center; vertical-align: middle'>" + message + "</p>")
                .dialog({
                    autoOpen: false,
                    modal: true,
                    title: '<span class="ui-icon ' + icon + '" style="float:left; margin:0 7px 0px 0;"></span>' + title,
                    resizable: false,
                    buttons: {
                        Ok: function() {
                            if ((onCloseFunction != undefined) && (typeof onCloseFunction == 'function'))
                                onCloseFunction();
                            $(this)
                                .dialog("close")
                                .dialog("destroy")
                                .remove();
                        }
                    },
                    close: function(e) {
                        if ($prevSibling.length)
                            $prevSibling.after($clone);
                        else
                            $parent.append($clone);
                    }
                })
                .dialog("open");
            }
        };
        
        
        $(function() {
            $("#ccyUd").combobox({size: 3});
            if ($("#actionError").length == 0 && $("#actionMessage").length == 0) {
                $("#rbBS").buttonset("destroy");
                $("#tempTitle").click();
                $("#tempButtons").click();
            } else {
                $("#rbBS").data("rdb").callCurrent();
                if ($("#actionError").length == 0) {
                    $("#rbBS").data("rdb").clear(false);
                }
            }
    
            $("label[for='frmMain_remarks']").parent().css('vertical-align','middle');
            $("#frmMain_noCif")
                    .parent()
                    .append(
                            $("#frmMain_namCustFull").detach()
                            )
                    ;
            
            $(".ui-autocomplete-input", $("#frmMain")).addClass("ui-widget ui-widget-content");        
            $(".ui-datepicker-trigger").before("&nbsp;");
            
            $("input[name^='noUd']")
                .css("text-transform", "uppercase")
                .blur(function() {
                    $(this).val($(this).val().toUpperCase());
                });
            
            /* formatting display of payee Country */
            $("#frmMain_label_valas_monitoring_payeeCountry").hide();
            $("label[for='frmMain_label_valas_monitoring_payeeCountry']").parent().after($("#payeeCountry").parent().detach());
            
            $("#frmMain_noUdSearch").parent().append("&nbsp;").append($("#btnFindUd").detach());
            $("#frmMain_txtTypeUD").parent().append("&nbsp;").append($("#btnTypeUDLookup").detach());
            $("#frmMain_txtPayeeCountry").parent().append("&nbsp;").append($("#btnPayeeCountryLookup").detach());
            
            $("#ccyUd").die('change');
            $("#ccyUd").live('change', function(e) {
                calcRatUsd($("#ccyUd").val());
                calcRatFcy($("#ccyUd").val());
                $("#frmMain_amtLimitScr").attr("value", null);
                $("#frmMain_amtLimitScrUSD").attr("value", null);
                $("#frmMain_ratFcyIdrScr").attr("value", null);
            });
            $("#frmMain_noCif").die('keydown');
            $("#frmMain_noCif").live('keydown', function(e) {
                if (e.keyCode == 9) {
                    var noCif = $.trim($("#frmMain_noCif").val());
                    if (noCif == "" || isNaN(noCif) || Number(noCif) < 0) {
                        if (isNaN(noCif))
                            alert('CIF No only accept numbers.');
                        e.preventDefault();
                        return;
                    }
    
                    if ($("#frmMain_namCustFull").val() == "") {
                        tempParams = {};
                        tempParams.onClose = function(namCustFull) {
                            if (namCustFull == "") {
                                alert("CIF: '" + $("#frmMain_noCif").val() + "' not found");
                                $("#frmMain_namCustFull").val("");
                                $("#frmMain_noCif").focus();
                                e.preventDefault();
                            } else {
                                $("#frmMain_namCustFull").val(namCustFull);
                                $("#frmMain_noCif").attr("readonly", "true");
                                if (!$("#rbBSAdd").attr("checked")) {
                                    $("#btnFindUd").button("enable");
                                }
                                else {
                                    $("#btnTypeUDLookup").button("enable");
                                    $("#payeeCountry_widget").attr("readonly", null);
                                    $(".ui-datepicker-trigger").show();
                                    $("#dtIssued")
                                        .attr("value", setIssuedDate)
                                        .attr("disabled", null);
                                    $("#dtExpiry")
                                        .attr("value", setExpiryDate)
                                        .attr("disabled", null);
                                    $("#frmMain_cdBranch").val('<s:property value="%{#session.cdBranch}" />');
                                    $("#frmMain_status").val("Active");
                                    $("#btnSave").show();
                                }
                            }
                        };
    
                        $("#formCifInfo_noCif").val($("#frmMain_noCif").val());
                        $("#tempCifInfo").click();
                    }
                }
            });
    
            $("#frmMain_noUdSearch").die('keydown');
            $("#frmMain_noUdSearch").live('keydown', function(e) {
                if (e.keyCode == 9) {
                    if (!$("#rbBSAdd").attr("checked")) {
                        $("#btnFindUd").click();
                    }
                }
            });
            
            $("#frmMain_noUd").blur(function() {
                var noUd = $(this).val();
                $.post(
                    "json/20302_checkUDNo",
                    {
                        noUd: noUd
                    },
                    function(data) {
                        $("#frmMain_noUdIsExist").attr("value", (data.noUd != "").toString());
                    },
                    "json"
                );
            });
    
            $("#frmMain_ratFcyIdrScr").focus(function() {
                if ($("#frmMain_ratFcyIdrScr").val() != '') {
                    $("#frmMain_ratFcyIdrScr").val($("#frmMain_ratFcyIdr").val());
                }
            });
    
            $("#frmMain_ratFcyIdrScr").blur(function(e) {
                var amtLimitScr = $.trim($("#frmMain_ratFcyIdrScr").val());
                if (isNaN(amtLimitScr) || Number(amtLimitScr) < 0) {
                    if (isNaN(amtLimitScr)) {
                        alert('FCY/IDR rate only accept numbers.');
                        $("#frmMain_ratFcyIdrScr").val("");
                        $("#frmMain_ratFcyIdrScr").focus();
                    }
                    e.preventDefault();
                    return;
                }
    
                if ($("#frmMain_ratFcyIdrScr").val() != '') {
    
                    $("#frmMain_ratFcyIdrScr").formatCurrency({decimalSymbol: '.', digitGroupSymbol: ',', symbol: ''});
                    var x = $("#frmMain_ratFcyIdrScr").val();
                    if (x.indexOf(',') != -1) {
                        while (x.indexOf(',') != -1) {
                            x = x.replace(",", "");
                        }
                    }
                    $("#frmMain_ratFcyIdr").val(Number(x));
                    //Limit USD Calculation
                    var limitUsd = $("#frmMain_amtLimit").val() * ($("#frmMain_ratFcyIdr").val() / $("#frmMain_usdIdrRate").val());
                    if ($("#ccyUd").val()=="USD"){
                        limitUsd = $("#frmMain_amtLimit").val();
                    }
                    //cek limit kelipatan 10000
                    var mod = limitUsd%5000; 
                    if (mod != 0){
                        if (limitUsd<5000)
                            limitUsd = 5000;
                        else
                            limitUsd = (limitUsd-mod)+5000;
                    }
                    $("#frmMain_amtLimitUSD").val(limitUsd);
                    $("#frmMain_amtLimitScrUSD").val(limitUsd);
                    $("#frmMain_amtLimitScrUSD").formatCurrency({decimalSymbol: '.', digitGroupSymbol: ',', symbol: ''});                
                }
            });        
            
            $("#frmMain_amtLimitScr").focus(function() {
                if ($("#frmMain_amtLimitScr").val() != '') {
                    $("#frmMain_amtLimitScr").val($("#frmMain_amtLimit").val());
                }
            });
    
            $("#frmMain_amtLimitScr").blur(function(e) {
                var amtLimitScr = $.trim($("#frmMain_amtLimitScr").val());
                if (isNaN(amtLimitScr) || Number(amtLimitScr) < 0) {
                    if (isNaN(amtLimitScr)) {
                        alert('Limit Amount only accept numbers.');
                        $("#frmMain_amtLimitScr").val("");
                        $("#frmMain_amtLimitScr").focus();
                    }
                    e.preventDefault();
                    return;
                }
    
                if ($("#frmMain_amtLimitScr").val() != '') {
    
                    $("#frmMain_amtLimitScr").formatCurrency({decimalSymbol: '.', digitGroupSymbol: ',', symbol: ''});
                    var x = $("#frmMain_amtLimitScr").val();
                    if (x.indexOf(',') != -1) {
                        while (x.indexOf(',') != -1) {
                            x = x.replace(",", "");
                        }
                    }
                    $("#frmMain_amtLimit").val(Number(x));
                    //Limit USD Calculation
                    var limitUsd = $("#frmMain_amtLimit").val() * ($("#frmMain_ratFcyIdr").val() / $("#frmMain_usdIdrRate").val());
                    if ($("#ccyUd").val()=="USD"){
                        limitUsd = $("#frmMain_amtLimit").val();
                    }
                    //cek limit kelipatan 10000
                    var mod = limitUsd%5000; 
                    if (mod != 0){
                        if (limitUsd<5000)
                            limitUsd = 5000;
                        else
                            limitUsd = (limitUsd-mod)+5000;
                    }
                    $("#frmMain_amtLimitUSD").val(limitUsd);
                    $("#frmMain_amtLimitScrUSD").val(limitUsd);
                    $("#frmMain_amtLimitScrUSD").formatCurrency({decimalSymbol: '.', digitGroupSymbol: ',', symbol: ''});
                }
            });
    
            $("#btnFindUd").click(function() {
                if (!($("#btnFindUd").button('option').disabled != undefined &&
                        $("#btnFindUd").button('option', 'disabled'))) {
                    dlgParams = {};
                    dlgParams.noCif = "frmMain_noCif";
                    dlgParams.noUd = "frmMain_noUd";
                    dlgParams.typeUD = "frmMain_typeUD";
                    dlgParams.payeeName = "frmMain_payeeName";
                    dlgParams.payeeCountry = "payeeCountry";
                    dlgParams.tradingProduct = "frmMain_tradingProduct";
                    dlgParams.dtIssued = "dtIssued";
                    dlgParams.dtExpiry = "dtExpiry";
                    dlgParams.ccyUd = "ccyUd";
                    dlgParams.amtLimit = "frmMain_amtLimit";
                    dlgParams.remarks = "frmMain_remarks";
                    dlgParams.cdBranch = "frmMain_cdBranch";
                    dlgParams.status = "frmMain_status";
                    dlgParams.ratFcyIdr = "frmMain_ratFcyIdr";
                    dlgParams.amtLimitUSD = "frmMain_amtLimitUSD";
                    dlgParams.usdIdrRate = "frmMain_usdIdrRate";
                    dlgParams.onClose = function() {
                        $("#ccyUd_combobox > input").val($("#ccyUd").val());
                        $("#frmMain_amtLimitScr").val($("#frmMain_amtLimit").val());
                        $("#frmMain_amtLimitScrUSD").val($("#frmMain_amtLimitUSD").val());
                        $("#frmMain_ratFcyIdrScr").val($("#frmMain_ratFcyIdr").val());
                        $("#frmMain_usdIdrRateScr").val($("#frmMain_usdIdrRate").val());
                        $("#frmMain_amtLimitScr").formatCurrency({decimalSymbol: '.', digitGroupSymbol: ',', symbol: ''});
                        $("#frmMain_amtLimitScrUSD").formatCurrency({decimalSymbol: '.', digitGroupSymbol: ',', symbol: ''});
                        $("#frmMain_ratFcyIdrScr").formatCurrency({decimalSymbol: '.', digitGroupSymbol: ',', symbol: ''});
                        $("#frmMain_usdIdrRateScr").formatCurrency({decimalSymbol: '.', digitGroupSymbol: ',', symbol: ''});
                        
                        if (rdb.current != 'inquiry') $("#btnSave").show();
                        
                        if (rdb.current == 'edit') {
                            $("#btnTypeUDLookup").button("enable");
                            $("#payeeCountry_widget").attr("readonly", null);
                            $(".ui-datepicker-trigger").show();
                            $("#dtIssued").attr("disabled", null);
                            $("#dtExpiry").attr("disabled", null);
                        }
                        
                        // lookup description
                        if ($("#" + dlgParams.typeUD).val().trim() != "")
                            lookupDataMaster("UnderlyingDocumentType", $("#" + dlgParams.typeUD).val(), null, "frmMain_txtTypeUD");
                        if ($("#" + dlgParams.payeeCountry).val().trim() != "")
                            lookupDataMaster("country", $("#" + dlgParams.payeeCountry).val(), null, "payeeCountry_widget");
                    };
                    $("#ph-dlg").dialog("option", "position", "center");
                    $("#ph-dlg").dialog("option", "width", $("body").width() * 3 / 4);
                    //$("#ph-dlg").dialog("option", "height", $("body").height()*3/4);
                    $("#ph-dlg").dialog("option", "height", "450");
                    $("#ph-dlg")
                            .html("Please wait...")
                            .unbind("dialogopen")
                            .bind("dialogopen", function() {
                                $("#tempFrmDlgMfcUd_noCif").val($("#frmMain_noCif").val());
                                $("#tempFrmDlgMfcUd_noUd").val($("#frmMain_noUdSearch").val());
                                $("#tempDlgMfcUd").click();
                            })
                            .dialog("open");
                }
            });
            
            $("#btnTypeUDLookup").click(function() {
                var $tmp = $("#ph-dlg").dialog("option", "title");
                $("#ph-dlg").dialog("option", "position", "center");
                $("#ph-dlg").dialog("option", "width", $("body").width() * 3 / 4);
                $("#ph-dlg").dialog("option", "height", "450");
                
                dlgParams = {};
                dlgParams.id = "frmMain_typeUD";
                dlgParams.desc = "frmMain_txtTypeUD";
                
                $("#ph-dlg")
                    .html("Please wait...")
                    .unbind("dialogopen")
                    .bind("dialogopen", function() {
                        $("#aDlgTypeUD").click();
                    })
                    .unbind("dialogclose")
                    .bind("dialogclose", function() {
                        $(this).dialog({title: $tmp});
                    })
                    .dialog("open");
            });
    
            $("#btnSave").subscribe("beforeSubmit", function(event) {
                $("#frmMain").unbind("submit");
                event.originalEvent.options.submit = false;
                
                var linkText = $("#frmMain_remarks").val();
                linkText = linkText.replace(/<\/?[^>]+(>|$)/g,"");   
                $("#frmMain_remarks").val(linkText);
                
                if (validateForm_frmMain()) {
                    dlgParams = {};
                    dlgParams.idMaintainedBy = "frmMain_idMaintainedBy";
                    dlgParams.idMaintainedSpv = "frmMain_idMaintainedSpv";
                    dlgParams.event = event;
                    dlgParams.onSubmit = function() {
                        dlgParams.event.originalEvent.options.submit = true;
                        $("#btnSave").unsubscribe("beforeSubmit");
                        $("#btnSave").click();
                    };
                    $("#ph-dlg").dialog("option", "position", "center");
                    //$("#ph-dlg").dialog("option", "width", $("body").width()*2/4);
                    //$("#ph-dlg").dialog("option", "height", $("body").height()*2/4);
                    $("#ph-dlg").dialog("option", "width", "320");
                    $("#ph-dlg").dialog("option", "height", "180");
                    $("#ph-dlg")
                            .html("Please wait...")
                            .unbind("dialogopen")
                            .bind("dialogopen", function() {
                                $("#tempDlgAuth").click();
                            })
                            .dialog("open");
                }
            });
            
            $("#tempDlgAuth")
                .unsubscribe("dlgAuthOpenedSuccess")
                .subscribe("dlgAuthOpenedSuccess", function(event) {
                    if ($("#frmMain_noUdIsExist").attr("value") == "true")
                        $("#frmDlg").find("tbody:first").prepend("<tr><td class='required' colspan='2' align='center'>No UD already used by other CIF</td><tr/>");
                });
            
            msgParams = {}; 
            msgParams.isOpen = false;
            msgParams.onClose = function(message) {
                   messageBox(3, message, function() { msgParams.isOpen = false; });
               };       
        });
    </script>
<% }%>