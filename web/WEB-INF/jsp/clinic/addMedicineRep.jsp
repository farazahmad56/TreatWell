<%-- 
    Document   : addMedicalRep
    Created on : Dec 16, 2017, 1:07:27 PM
    Author     : Wasi
--%>

<%@include file="../header.jsp"%>
<script type="text/javascript" src="assets/global/plugins/jquery-inputmask/jquery.inputmask.bundle.min.js"></script>
<script>
    $(function () {
//        $("#contactNo").inputmask("mask", {
//            "mask": "99999999999"
//        });
        $('#pharmaCompanyId').change(function () {
            displayData();
        }).trigger('change');
    });
    function displayData() {
        var $tbl = $('<table class="table table-striped table-bordered table-hover">');
        $tbl.append($('<thead>').append($('<tr>').append(
                $('<th class="center" width="5%">').html('Sr. #'),
                $('<th class="center" width="30%">').html('Full Name'),
                $('<th class="center" width="20%">').html('Contact No#'),
                $('<th class="center" width="15%">').html('Designation'),
                $('<th class="center" width="15%" colspan="2">').html('&nbsp;')
                )));
        $.get('clinic.htm?action=getMedicinesRep', {pharmaCompanyId: $('#pharmaCompanyId').val()},
                function (list) {
                    if (list !== null && list.length > 0) {
                        $tbl.append($('<tbody>'));
                        for (var i = 0; i < list.length; i++) {
                            var editHtm = '<i class="fa fa-pencil-square-o" aria-hidden="true" title="Click to Edit" style="cursor: pointer;" onclick="editRow(\'' + list[i].TW_PHARMA_RAP_ID + '\');"></i>';
                            var delHtm = '<i class="fa fa-trash-o" aria-hidden="true" title="Click to Delete" style="cursor: pointer;" onclick="deleteRow(\'' + list[i].TW_PHARMA_RAP_ID + '\');"></i>';
                            if ($('#can_edit').val() !== 'Y') {
                                editHtm = '&nbsp;';
                            }
                            if ($('#can_delete').val() !== 'Y') {
                                delHtm = '&nbsp;';
                            }
                            $tbl.append(
                                    $('<tr>').append(
                                    $('<td  align="center">').html(eval(i + 1)),
                                    $('<td>').html(list[i].FULL_NME),
                                    $('<td>').html(list[i].CONTACT_NO),
                                    $('<td>').html(list[i].DESIGNITION),
                                    $('<td align="center">').html(editHtm),
                                    $('<td  align="center">').html(delHtm)
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

    function saveData() {
        if ($.trim($('#fullName').val()) === '') {
            $('#fullName').notify('Full Name is Required Field', 'error', {autoHideDelay: 15000});
            $('#fullName').focus();
            return false;
        }

        if ($.trim($('#pharmaCompanyId').val()) === '') {
            $('#pharmaCompanyId').notify('Please select manufacturer company.', 'error', {autoHideDelay: 15000});
            $('#pharmaCompanyId').focus();
            return false;
        }
        if ($.trim($('#contactNo').val()) === '') {
            $('#contactNo').notify('Please select contact no.', 'error', {autoHideDelay: 15000});
            $('#contactNo').focus();
            return false;
        }
        if ($('#contactNo').val().length < 11) {
            $('#contactNo').notify('Please enter correct contact no.', 'error', {autoHideDelay: 15000});
            $('#contactNo').focus();
            return false;
        }
        var obj = {
            pharmaRepId: $('#pharmaRepId').val(),
            fullName: $('#fullName').val(),
            contactNo: $('#contactNo').val(),
            designation: $('#designation').val(),
            pharmaCompanyId: $('#pharmaCompanyId').val()
        };
        $.post('clinic.htm?action=saveMedicineRep', obj, function (obj) {
            if (obj.result === 'save_success') {
                $.bootstrapGrowl("Medicine Rep saved successfully.", {
                    ele: 'body',
                    type: 'success',
                    offset: {from: 'top', amount: 80},
                    align: 'right',
                    allow_dismiss: true,
                    stackup_spacing: 10
                });
                $('input:text').val('');
                $('#pharmaRepId').val('');
                $('#addMedicineRep').modal('hide');
                displayData();
                return false;
            } else {
                $.bootstrapGrowl("Error in saving Medicine Rep. Please try again later.", {
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
        return false;
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
                    $.post('clinic.htm?action=deleteMedicineRep', {id: id}, function (res) {
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
    function addMedicineRepDialog() {
        $('#pharmaRepId').val('');
        $('#fullName').val('');
        $('#contactNo').val('');
        $('#contactNo').attr('readOnly', false);
        $('#addMedicineRep').modal('show');
    }
    function editRow(id) {
        $('#pharmaRepId').val(id);
        $.get('clinic.htm?action=getMedicineRepById', {medicineRepId: id},
                function (obj) {
                    $('#fullName').val(obj.FULL_NME);
                    $('#contactNo').val(obj.CONTACT_NO);
                    $('#contactNo').attr('readOnly', true);
                    $('#designation').val(obj.DESIGNITION);
                    $('#addMedicineRep').modal('show');
                }, 'json');
    }

</script>
<div class="page-head">
    <!-- BEGIN PAGE TITLE -->
    <div class="page-title">
        <h1>Medical Rep</h1>
    </div>
</div>
<div class="modal fade" id="addMedicineRep">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h3 class="modal-title">Add Medical Rep</h3>
            </div>
            <div class="modal-body">
                <input type="hidden" id="pharmaRepId" value="">
                <form action="#" role="form" method="post" >
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group">
                                <label>Full Name *</label>
                                <div>
                                    <input type="text" class="form-control" id="fullName" placeholder="Full Name" >
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Contact No. *</label>
                                <div>
                                    <input type="text" class="form-control" id="contactNo" placeholder="03001111111" onkeyup="onlyInteger(this);" onblur="Util.validateUser(this);" >
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Designation</label>
                                <select id="designation" class="form-control">
                                    <option value="SALES REP" selected="">Sales Rep</option>
                                    <option value="CEO">CEO</option>
                                    <option value="MANAGER">Manager</option>
                                </select>
                            </div>
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
        <div class="portlet box green">
            <div class="portlet-title tabbable-line">
                <div class="caption">
                    Medical Rep
                </div>
            </div>
            <div class="portlet-body">
                <input type="hidden" id="can_edit" value="${requestScope.refData.CAN_EDIT}">
                <input type="hidden" id="can_delete" value="${requestScope.refData.CAN_DELETE}">
                <form action="#" onsubmit="return false;" role="form" method="post">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group">
                                <label>Pharmaceutical Company</label>
                                <select id="pharmaCompanyId" class="form-control" onchange="displayData();">
                                    <c:forEach items="${requestScope.refData.companies}" var="obj">
                                        <option value="${obj.TW_PHARMACEUTICAL_ID}">${obj.COMPANY_NME}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-2" style="padding-top: 23px;">
                            <c:if test="${requestScope.refData.CAN_ADD=='Y'}">
                                <button type="button" class="btn blue" onclick="addMedicineRepDialog();"><i class="fa fa-plus-circle"></i> New Medical Rep</button>
                            </c:if>
                        </div>
                    </div>
                    <br/>
                    <div class="row">
                        <div class="col-md-12">
                            <div id="displayDiv"></div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<%@include file="../footer.jsp"%>

