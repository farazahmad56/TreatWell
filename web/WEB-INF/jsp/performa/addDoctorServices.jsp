<%-- 
    Document   : addDoctorServices
    Created on : Oct 17, 2017, 7:20:28 PM
    Author     : Cori 5
--%>

<%@include file="../header.jsp"%>
<script language='javascript' src="js/md5.js" type="text/javascript"></script>
<script>
    $(function () {

        if ($('#userType').val() === 'ADMIN') {
            $('#doctorId').select2({
                placeholder: "Select an option",
                allowClear: true
            });
        }
        displayData();
        displayPanelCompany();
    });
    function saveData() {
        var fee = [], discount = [], panelComapnyId = [];
        if ($('#procedureId').val() !== '') {
            panelComapnyId.push($('#compId').val());
        } else {
            $.each($('input[name=companyId]'), function (index, obj) {
                panelComapnyId.push($(obj).val());
            });
        }
        $.each($('input[name=fee]'), function (index, obj) {
            ($(obj).val() !== '' ? fee.push($(obj).val()) : fee.push('0'));
        });
        $.each($('input[name=discount]'), function (index, obj) {
            ($(obj).val() !== '' ? discount.push($(obj).val()) : discount.push('0'));
        });
        if (fee.length > 0) {
            $.post('performa.htm?action=saveDoctorProcedure', {
                procedureId: $('#procedureId').val(),
                'panelCompanyIdArr[]': panelComapnyId,
                'feeArr[]': fee,
                'discountArr[]': discount,
                serviceId: $('#serviceId').val(),
                doctorId: $('#doctorId').val()
            }, function (obj) {
                if (obj.result === 'save_success') {
                    $.bootstrapGrowl("Doctor's Procedure Fee saved successfully.", {
                        ele: 'body',
                        type: 'success',
                        offset: {from: 'top', amount: 80},
                        align: 'right',
                        allow_dismiss: true,
                        stackup_spacing: 10
                    });
                    $('input:text').val('');

                    $('textarea').val('');
                    $('#addProduct').modal('hide');
                    displayData();
                    return false;
                } else {
                    $.bootstrapGrowl("Error in saving Doctor's Procedure Fee. Please try again later.", {
                        ele: 'body',
                        type: 'danger',
                        offset: {from: 'top', amount: 80},
                        align: 'right',
                        allow_dismiss: true,
                        stackup_spacing: 10
                    });
                    return false;
                }
            }, 'json');
        } else {
            $.bootstrapGrowl("Please Enter Fee.", {
                ele: 'body',
                type: 'danger',
                offset: {from: 'top', amount: 80},
                align: 'right',
                allow_dismiss: true,
                stackup_spacing: 10
            });
            return false;
        }
    }


    function editRow(id, companyName) {
        $('#panelCompanyTable').find('tbody').find('tr').not('tr:first').remove();
        $('#procedureId').val(id);
        getProcedure();
        $.get('performa.htm?action=getProcedureFeeById', {procedureId: id},
                function (obj) {
                    $('#companyName').text(companyName);
                    $('#fee').val(obj.FEE);
                    $('#discount').val(obj.DISCOUNT);
                    $('#serviceId').val(obj.TW_MEDICAL_PROCEDURE_ID);
                    $('#addProduct').modal('show');
                }, 'json');
    }


    function deleteRow(id) {
        bootbox.confirm({
            message: "Do you want to delete record?",
            buttons: {
                confirm: {
                    label: 'Yes',
                    className: 'btn-success'
                },
                cancel: {
                    label: 'No',
                    className: 'btn-danger'
                }
            },
            callback: function (result) {
                if (result) {
                    $.post('performa.htm?action=deleteDoctorProcedure', {id: id}, function (res) {
                        if (res.result === 'save_success') {
                            $.bootstrapGrowl("Record deleted successfully.", {
                                ele: 'body',
                                type: 'success',
                                offset: {from: 'top', amount: 80},
                                align: 'right',
                                allow_dismiss: true,
                                stackup_spacing: 10
                            });
                            displayData();
                        } else {
                            $.bootstrapGrowl("Record can not be deleted.", {
                                ele: 'body',
                                type: 'danger',
                                offset: {from: 'top', amount: 80},
                                align: 'right',
                                allow_dismiss: true,
                                stackup_spacing: 10
                            });
                        }
                    }, 'json');

                }
            }
        });
    }
    function displayPanelCompany() {
        $('#compId').find('option').remove();
        $('<option />', {value: '', text: 'General patient'}).appendTo($('#compId'));
        $.get('setup.htm?action=getPanelCompaniesForDoctors', {doctorId: $('#doctorId').val()},
                function (list) {
                    if (list !== null && list.length > 0) {

                        for (var i = 0; i < list.length; i++) {
                            $('<option />', {value: list[i].TW_COMPANY_ID, text: list[i].COMPANY_NME}).appendTo($('#compId'));

                        }
                    }
                }, 'json');
    }
    function getProcedure() {
        $('#serviceId').find('option').remove();
        $.get('clinic.htm?action=getProcedure',
                function (list) {
                    if (list !== null && list.length > 0) {

                        for (var i = 0; i < list.length; i++) {
                            $('<option />', {value: list[i].TW_MEDICAL_PROCEDURE_ID, text: list[i].TITLE}).appendTo($('#serviceId'));

                        }
                    }
                }, 'json');
    }
    function displayPanelCompanyData() {
        getProcedure();
        $.get('setup.htm?action=getPanelCompaniesForDoctors', {doctorId: $('#doctorId').val()},
                function (list) {
                    if (list !== null && list.length > 0) {
                        var compHmt = '';
                        for (var i = 0; i < list.length; i++) {
                            compHmt += '<tr><td>\n\
                                        <input name="companyId" type="hidden" value="' + list[i].TW_COMPANY_ID + '"><label>' + list[i].COMPANY_NME + '</label></td>\n\
                                    <td><input type="text"   class="form-control" name="fee" value="0" placeholder="Fee" onkeyup="onlyDouble(this);"  ></td>\n\
                                    <td><input type="text" class="form-control" name="discount" value="0" placeholder="Discount" onkeyup="onlyDouble(this);"  maxlength="2" >';

                        }
                        $('#panelCompanyTable').find('tbody').find('tr').not('tr:first').remove();
                        $('#panelCompanyTable tbody').append(compHmt);
                        $('#serviceId').find('option:first').attr('selected', 'selected');
                        $('input[type=text]').val('');
                        $('#procedureId').val('');
                        $('#addProduct').modal('show');
                    } else {
                        $('#serviceId').find('option:first').attr('selected', 'selected');
                        $('input[type=text]').val('');
                        $('#procedureId').val('');
                        $('#addProduct').modal('show');
                    }
                }, 'json');
    }
    function displayData() {
        var $tbl = $('<table class="table table-striped table-bordered table-hover">');
        $tbl.append($('<thead>').append($('<tr>').append(
                $('<th class="center" width="5%">').html('Sr. #'),
                $('<th class="center" width="20%">').html('Procedure Name'),
                $('<th class="center" width="20%">').html('Fee'),
                $('<th class="center" width="20%">').html('Discount (%)'),
                $('<th class="center" width="20%">').html('Company Name'),
                $('<th class="center" width="15%" colspan="2">').html('&nbsp;')
                )));
        $.get('performa.htm?action=getDoctorProcedure', {doctorList: $('#doctorId').val(), companyId: $('#compId').val()},
                function (list) {
                    if (list !== null && list.length > 0) {
                        $tbl.append($('<tbody>'));
                        for (var i = 0; i < list.length; i++) {
                            $tbl.append(
                                    $('<tr>').append(
                                    $('<td  align="center">').html(eval(i + 1)),
                                    $('<td>').html(list[i].TITLE),
                                    $('<td>').html(list[i].FEE),
                                    $('<td>').html(list[i].DISCOUNT),
                                    $('<td>').html(list[i].COMPANY_NME),
                                    $('<td align="center">').html('<i class="fa fa-pencil-square-o" aria-hidden="true" title="Click to Edit" style="cursor: pointer;" onclick="editRow(\'' + list[i].TW_PROCEDURE_FEE_ID + '\',\'' + list[i].COMPANY_NME + '\');"></i>'),
                                    $('<td align="center">').html('<i class="fa fa-trash-o" aria-hidden="true" title="Click to Delete" style="cursor: pointer;" onclick="deleteRow(\'' + list[i].TW_PROCEDURE_FEE_ID + '\');"></i>')
                                    ));
                        }
                        $('#displayDiv').html('');
                        $('#displayDiv').append($tbl);
                        return false;
                    } else {
                        $('#displayDiv').html('');
                        $tbl.append(
                                $('<tr>').append(
                                $('<td  colspan="7">').html('<b>No data found.</b>')
                                ));
                        $('#displayDiv').append($tbl);
                        return false;
                    }
                }, 'json');
    }
</script>
<div class="page-head">
    <!-- BEGIN PAGE TITLE -->
    <div class="page-title">
        <h1>Procedure's Fee</h1>
    </div>
</div>
<div class="modal fade" id="addProduct">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h3 class="modal-title">Procedure Fee</h3>
            </div>
            <div class="modal-body">
                <input type="hidden" id="procedureId" value="">
                <form action="#" role="form" method="post" >
                    <div class="row">                    
                        <div class="col-md-12">
                            <div class="form-group">
                                <label>Procedure Name</label>
                                <select id="serviceId" class="form-control">
                                    <option value="">Select Procedure</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <table class="table table-striped table-bordered table-hover" id="panelCompanyTable">
                                <thead>
                                <th width="60%">Company Name</th>
                                <th width="20%">Fee</th>
                                <th width="20%">Discount (%)</th>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td><input name="companyId" type="hidden" value=""><label id="companyName">General Patient</label></td>
                                        <td><input type="text"   class="form-control" id="fee" name="fee" value="0" placeholder="Fee" onkeyup="onlyDouble(this);"  ></td>
                                        <td><input type="text" class="form-control" id="discount" name="discount" value="0" placeholder="Discount" onkeyup="onlyDouble(this);"  maxlength="2" ></td>
                                    </tr>

                                </tbody>
                            </table>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="saveData();">Save</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<div class="row">
    <div class="col-md-12">
        <div class="portlet-body">
            <form name="doctorform" action="#" role="form" onsubmit="return false;" method="post">
                <input type="hidden" id="userType" value="${requestScope.refData.userType}">
                <div class="portlet box green">
                    <div class="portlet-title">
                        <div class="caption">
                            Procedures
                        </div>
                    </div>
                    <div class="portlet-body">
                        <div class="row">
                            <div class="col-md-4">
                                <div class="form-group">
                                    <label>Company Name</label>
                                    <select id="compId" class="form-control" onchange="displayData();">
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <c:choose>
                                    <c:when test="${requestScope.refData.userType=='ADMIN'}">

                                        <div class="form-group">
                                            <label>Doctor Name</label>
                                            <select id="doctorId" class="form-control" onchange="displayData();">
                                                <c:forEach items="${requestScope.refData.doctorNames}" var="obj">
                                                    <option value="${obj.TW_DOCTOR_ID }">${obj.DOCTOR_NME}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </c:when>
                                    <c:when test="${requestScope.refData.userType=='DOCTOR'}">
                                        <input type="hidden" id="doctorId" value="${requestScope.refData.doctorId}">
                                    </c:when>
                                </c:choose>
                            </div>
                            <div class="col-md-4 text-right" style="padding-top: 23px; " >
                                <button type="button" class="btn blue" onclick="displayPanelCompanyData();"><i class="fa fa-plus-circle"></i> Procedure Fee</button>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-12" style="padding-top: 20px;">
                                <div id="displayDiv">

                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </form>
        </div>
    </div>
</div>
<%@include file="../footer.jsp"%>
