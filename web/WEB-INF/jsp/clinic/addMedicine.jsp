<%-- 
    Document   : addMedicine
    Created on : Nov 21, 2017, 5:07:27 PM
    Author     : Cori 5
--%>

<%@include file="../header.jsp"%>
<script>
    $(function () {
        $('#selectDiseases').select2({
            placeholder: "Select an option",
            allowClear: true
        });
        $('#pharmaCompanyId').change(function () {
            displayData();
        });
        displayData();
    });
    function displayData() {
        var $tbl = $('<table class="table table-striped table-bordered table-hover">');
        $tbl.append($('<thead>').append($('<tr>').append(
                $('<th class="center" width="5%">').html('Sr. #'),
                $('<th class="center" width="30%">').html('Medicine Name'),
                $('<th class="center" width="20%">').html('Generic Name'),
                $('<th class="center" width="30%">').html('Manufacturer'),
                $('<th class="center" width="15%" colspan="3">').html('&nbsp;')
                )));
        $.get('clinic.htm?action=getMedicines', {pharmaCompanyId: $('#pharmaCompanyId').val()},
                function (list) {
                    if (list !== null && list.length > 0) {
                        $tbl.append($('<tbody>'));
                        for (var i = 0; i < list.length; i++) {
                            var editHtm = '<i class="fa fa-pencil-square-o" aria-hidden="true" title="Click to Edit" style="cursor: pointer;" onclick="editRow(\'' + list[i].TW_MEDICINE_ID + '\');"></i>';
                            var delHtm = '<i class="fa fa-trash-o" aria-hidden="true" title="Click to Delete" style="cursor: pointer;" onclick="deleteRow(\'' + list[i].TW_MEDICINE_ID + '\');"></i>';
                            if ($('#can_edit').val() !== 'Y') {
                                editHtm = '&nbsp;';
                            }
                            if ($('#can_delete').val() !== 'Y') {
                                delHtm = '&nbsp;';
                            }
                            $tbl.append(
                                    $('<tr>').append(
                                    $('<td  align="center">').html(eval(i + 1)),
                                    $('<td>').html(list[i].PRODUCT_NME),
                                    $('<td>').html(list[i].GENERIC_NME),
                                    $('<td>').html(list[i].COMPANY_NME),
                                    $('<td align="center">').html('<i class="fa fa-paperclip" aria-hidden="true" title="Click to Attach" style="cursor: pointer;" onclick="addAttachments(\'' + list[i].TW_MEDICINE_ID + '\');"></i>'),
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
        if ($.trim($('#medicineName').val()) === '') {
            $('#medicineName').notify('Medicine Name is Required Field', 'error', {autoHideDelay: 15000});
            $('#medicineName').focus();
            return false;
        }

        if ($.trim($('#pharmaCompanyId').val()) === '') {
            $('#pharmaCompanyId').notify('Please select manufacturer company.', 'error', {autoHideDelay: 15000});
            $('#pharmaCompanyId').focus();
            return false;
        }
        var obj = {
            productId: $('#medicinesId').val(),
            productName: $('#medicineName').val(),
            productGenericName: $('#genericName').val(),
            therapauticClass: $('#therapauticClass').val(),
            productType: $('#productType').val(),
            'selectDiseasesArr[]': $('#selectDiseases').val(),
            productFeatures: $('#productFeatures').val(),
            pharmaCompanyId: $('#pharmaCompanyId').val()
        };
        $.post('clinic.htm?action=saveMedicine', obj, function (obj) {
            if (obj.result === 'save_success') {
                $.bootstrapGrowl("Medicine saved successfully.", {
                    ele: 'body',
                    type: 'success',
                    offset: {from: 'top', amount: 80},
                    align: 'right',
                    allow_dismiss: true,
                    stackup_spacing: 10
                });
                $('input:text').val('');
                $('#medicinesId').val('');
                $('#addMedicine').modal('hide');
                displayData();
                return false;
            } else {
                $.bootstrapGrowl("Error in saving Medicine. Please try again later.", {
                    ele: 'body',
                    type: 'error',
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
                    $.post('clinic.htm?action=deleteMedicine', {id: id}, function (res) {
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
    function addMedicineDialog() {
        $('#medicinesId').val('');
        $('#medicineName').val('');
        $('#genericName').val('');
        $('#therapauticClass').val('');
        $('#selectDiseases').val('');
        $('#productFeatures').val('');
        $('#addMedicine').modal('show');
    }
    function addAttachments(id){
        $('#medicinesId').val(id);
        $('#addAttachments').modal('show');
    }
    function saveAttachmentData(){
        var data = new FormData(document.getElementById('addAttachmentsForm'));
        $.ajax({
            url: 'clinic.htm?action=saveMedicineAttachments',
            type: "POST",
            data: data,
            cache: false,
            dataType: 'json',
            processData: false, // tell jQuery not to process the data
            contentType: false   // tell jQuery not to set contentType

        }).done(function (data) {
            if (data) {
                if (data.result === 'save_success') {
                    $.bootstrapGrowl("Attachment Upload successfully.", {
                        ele: 'body',
                        type: 'success',
                        offset: {from: 'top', amount: 80},
                        align: 'right',
                        allow_dismiss: true,
                        stackup_spacing: 10

                    });
                    $('#addAttachments').modal('hide');

                } else {
                    $.bootstrapGrowl("Error in Attachment Uploading.", {
                        ele: 'body',
                        type: 'danger',
                        offset: {from: 'top', amount: 80},
                        align: 'right',
                        allow_dismiss: true,
                        stackup_spacing: 10
                    });
                    $('#addAttachments').modal('hide');
                }
            }
        });
    }
    function editRow(id) {
        $('#medicinesId').val(id);
        $.get('clinic.htm?action=getMedicineById', {medicineId: id},
                function (obj) {
                    $('#medicineName').val(obj.PRODUCT_NME);
                    $('#genericName').val(obj.GENERIC_NME);
                    $('#therapauticClass').val(obj.THERAPEUTIC_CLASS);
                    $('#productFeatures').val(obj.PRODUCT_DESC);
                    $('#productType').val(obj.TW_MEDICINE_TYP_ID);
                    var diseases = obj.DISEASES;
                    if (diseases !== null && diseases.length > 0) {
                        var arr = diseases.split(',');
                        $('#selectDiseases').val(arr);
                        $('#selectDiseases').trigger('change');
                    }
                    $('#addMedicine').modal('show');
                }, 'json');
    }

</script>
<div class="page-head">
    <!-- BEGIN PAGE TITLE -->
    <div class="page-title">
        <h1>Medicine</h1>
    </div>
</div>
<div class="modal fade" id="addAttachments">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h3 class="modal-title">Add Attachments</h3>
            </div>
            <div class="modal-body">
                <form action="#" role="form" id="addAttachmentsForm" method="post" >
                    <input type="hidden" name="productId" id="medicinesId" value="">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group">
                                <label>Choose File</label>
                                <input type="file" name="medicineImage" id="medicineImage" >
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="saveAttachmentData();">Save</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>

    </div>
</div>
<div class="modal fade" id="addMedicine">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h3 class="modal-title">Add Medicine</h3>
            </div>
            <div class="modal-body">
                <form action="#" role="form" method="post" >
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group">
                                <label>Product Type</label>
                                <select id="productType" class="form-control">
                                    <c:forEach items="${requestScope.refData.medicineType}" var="obj">
                                        <option value="${obj.TW_MEDICINE_TYP_ID}">${obj.TW_MEDICINE_TYP_NME}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group">
                                <label>Medicine Name*</label>
                                <div>
                                    <input type="text" class="form-control" id="medicineName" placeholder="Medicine Name" >
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group">
                                <label>Generic Name</label>
                                <div>
                                    <input type="text" class="form-control" id="genericName" placeholder="Generic Name" >
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group">
                                <label>Therapeutic Class</label>
                                <div>
                                    <input type="text" class="form-control" id="therapauticClass" placeholder="Therapeutic Class" >
                                </div>
                            </div>
                        </div> 
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group">
                                <label>Recommendations For Diseases</label>
                                <select id="selectDiseases"  class=" select2_category form-control" name="recommendationDiseases[]" multiple="multiple">
                                    <c:forEach items="${requestScope.refData.diseases}" var="obj">
                                        <option value="${obj.TW_DISEASE_ID}">${obj.TITLE}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group">
                                <label>Additional Features</label>
                                <textarea class="form-control" id="productFeatures" rows="2" cols="30"></textarea>
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
                    Medicine
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
                                <button type="button" class="btn blue" onclick="addMedicineDialog();"><i class="fa fa-plus-circle"></i> New Medicine</button>
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

