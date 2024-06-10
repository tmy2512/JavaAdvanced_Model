
// link api
var apiDepartmentURL = "http://localhost:8080/api/v1/departments";
var apiAccountURL = "http://localhost:8080/api/v1/accounts";

//
function isLogin(){
    if(storage.getItem("ID")){
        return true;
    }
    return false;
}

// load header, main and footer
$(function () {

        $(".header").load("/templates/header.html", function(){
        
        });
        
        $(".footer").load("/templates/footer.html");


});

// đi đến trang chủ
function viewHome() {
    $(".main").load("/templates/home.html", function(){
        document.getElementById("user-full-name").innerHTML = storage.getItem("FULL_NAME")
    });
}

// đến trang quản lý department
function viewDeparment() {
    $(".main").load("/department/department.html", function (){
        
        initDepartmentTable("");
        searchByEnterKey();
        initDapartmentTypeList();
        // showAlertWarning();
    });
}

// đến trang quản lý account
function viewAccount() {
    $(".main").load("/account/account.html", function() {
        initAccountTable("");
        initAccountRoleList();
        initAccountDepartmentList();
        searchAccountByEnterKey();
        
    });
}

// ẩn modal tạo mới department
function hideAddNewModal() {
    $("#add-new-department").modal("hide");
    $("#add-account-button button").hide();
    resetDepartmentModal();
}

// ẩn add account to department modal
function hideAddAccountToDepartmentModal() {
    $("#add-account-to-department").modal("hide");
    resetDepartmentModal();
}

// thông báo 
function showAlertSuccess() {
    $("#alert-success").fadeTo(5000, 200).slideUp(2000, function () {
        $("#alert-success").slideUp(5000);
    });
}

// param để truyền vào department api(search, filter, pageable)
let getApiParam = {
    pageNumber: 0,
    search: "",
    minDate: "",
    maxDate: "",
    type: "",
    getParamString() {
        return "?pageNumber=" + this.pageNumber +
            "&search=" + this.search +
            "&minCreatedDate=" + this.minDate +
            "&maxCreatedDate=" + this.maxDate +
            "&type=" + this.type;
    }
};

// Phân trang
function pagination(result) {
    const activeNumber = result.number + 1;
    $('#pagination-department').html(`<a title="Previous" href = "javascript:void(0)" onclick="toDepartmentPage(${activeNumber > 1 ? activeNumber - 1 : activeNumber})">Previous</a>`);
    for (let i = 0; i < result.totalPages; i++) {
        const curNumber = i + 1;
        if (curNumber == activeNumber) {
            $('#pagination-department').append(`<a href = "javascript:void(0)" class="active">${curNumber}</a>`);
        } else {
            $('#pagination-department').append(`<a href = "javascript:void(0)" onclick="toDepartmentPage(${curNumber.toString()})">${curNumber}</a>`);
        }
    }
    $('#pagination-department').append(`<a href = "javascript:void(0)" title="Next" onclick="toDepartmentPage(${activeNumber < result.totalPages ? activeNumber + 1 : activeNumber})">Next</a>`);
}

// chuyển tới trang số ***
function toDepartmentPage(number) {
    getApiParam.pageNumber = number;
    initDepartmentTable(getApiParam.getParamString());
}

// Lấy danh sách cho bảng Department
function openAddNewDepartmentModal() {
    $("#add-new-department").modal("show");
    $("#add-account-button button").show();
    $("#add-new-department-title").html("Create New Department")
    resetDepartmentModal();
    // khởi tạo danh sách type cho department
    initDapartmentTypeList();
    // khởi tạo danh sách account để thêm vào department
    initAccountTableToAddToDepartment();
}

// reset lại department modal
function resetDepartmentModal() {
    document.getElementById("department-id").value = "";
    document.getElementById("department-name").value = "";
    document.getElementById("department-type-select").value = "";
}


// khởi tạo danh sách department
function initDepartmentTable(param) {
   // alert(123);
    $('tbody').empty();
    $.ajax({
        url: apiDepartmentURL + param,
        type: 'GET',
        success: function (result) {
            console.log(result);
            pagination(result);
            var list = result.content;
            list.forEach(function (item) {
                $('tbody').append(
                    `<tr>
                    <td><input type='checkbox' name='check-input' value='${item.id}'></td>
                    <td>${item.name}</td>'
                    <td>${item.totalMember}</td>
                    <td>${item.type}</td>
                    <td>${item.createdDate}</td>
                    <td>
                    <a class="add-member" title="Add Member" data-toggle="tooltip" onclick ="opendAddAccountToDepartmentModal(${item.id})"><i class="material-icons">add_circle</i></a>
                    <a class="edit" title="Edit" data-toggle="tooltip" onclick ="opendUpdateDepartmentModal(${item.id})"><i class="material-icons">&#xE254;</i></a>
                    <a class="delete" title="Delete" data-toggle="tooltip" onclick ="openDeleteDepartmentModal(${item.id})"><i class="material-icons">&#xE872;</i></a>
                    </td>
                    </tr>`
                )
            });
        },
    });
}

// lấy danh sách account để add vào department
function initAccountTableToAddToDepartment() {
    $("#account-list-table tbody").empty();
    $.ajax({
        url: apiAccountURL + "/list",
        type: 'GET',
        async: false,
        // beforeSend: function (xhr) {
        //     xhr.setRequestHeader("Authorization", "Basic " + btoa(storage.getItem("USERNAME") + ":" + storage.getItem("PASSWORD")));
        // }
        // ,
        success: function (result) {
            result.content.forEach(function (item) {
                $("#account-list-table tbody").append(
                    '<tr>' +
                    "<td><input type='checkbox' name='check-account' value='" + item.id + "'></td>" +
                    '<td>' + item.username + '</td>' +
                    '<td>' + item.fullName + '</td>' +
                    '<td>' + item.role + '</td>' +
                    '</tr>'
                )
            });
        }
    });
}

// tìm kiếm bằng phím enter
function searchByEnterKey() {
    $("#search-by-name").keydown((event) => {
        let keyCode = event.which
        if (keyCode == 13) {
            searchDepartmentByName();
        }
    })
}

// tìm phòng ban theo tên phòng ban
function searchDepartmentByName() {
    var name = document.getElementById("search-by-name").value;
    getApiParam.search = name;
    initDepartmentTable(getApiParam.getParamString());

}

// khởi tạo danh sách department có filter
function initDepartmentTableWithFilter() {

    var type = document.getElementById("department-type-filter").value;
    var minDate = document.getElementById("min-date").value;
    var maxDate = document.getElementById("max-date").value;
    // filter theo type
    getApiParam.type = type;
    // filter theo min date
    getApiParam.minDate = minDate;
    // filter theo max date
    getApiParam.maxDate = maxDate;
    initDepartmentTable(getApiParam.getParamString());

}


// lấy mảng id của những account cần thêm vào department
function getArrayID() {
    var listIDs = new Array();
    var checkboxes = document.getElementsByName('check-account');
    for (var checkbox of checkboxes) {
        if (checkbox.checked) {
            listIDs.push({ id: checkbox.value });
        }
    }
    return listIDs;
}

// lấy mảng id của những account sẽ xóa
function getArrayIdToDelete() {
    var listIDs = new Array();
    var checkboxes = document.getElementsByName('check-input');
    for (var checkbox of checkboxes) {
        if (checkbox.checked) {
            listIDs.push(checkbox.value);
        }
    }
    return listIDs;
}

// khởi tạo danh sách department type cho drop box của department
function initDapartmentTypeList() {
    $('.form-select').empty();
    $('.form-select').html(`<option value="">--Choose a type--</option>`);

    $.ajax({
        url: apiDepartmentURL + "/types",
        type: 'GET',
        success: function (result) {
            result.forEach(function (item) {
                console.log(item);
                $('.form-select').append(
                    '<option value="' + item.typeValue + '">' + item.typeName + '</option>'
                )
            });
        }
    });

}

// save department (create new department or update department)
function saveDepartment() {
    var departmentID = document.getElementById("department-id").value;
    if (departmentID == undefined || departmentID == "") {
        createNewDepartment();
    } else {
        updateDepartment(departmentID);
    }
}

// tạo mới department
function createNewDepartment() {
    let name = document.getElementById("department-name").value;
    let type = document.getElementById("department-type-select").value;
    let accounts = getArrayID();
    let totalMember = accounts.length
    let department = {
        name: name,
        totalMember: totalMember,
        type: type,
        accounts: accounts
    }

    if(name.length < 1 || name.length > 30){ 
        alert("Độ dài name không hợp lệ");
        return;
    }

    let isOk = true;
 
    // gọi api thêm department mới
    $.ajax({
        url: apiDepartmentURL,
        type: 'POST',
        data: JSON.stringify(department), // body
        contentType: "application/json",
        async: false,
        success: function (result) { 
            isOk = true;
        },
        error: function(xhr){
            isOk = false;
            console.log(xhr.responseJSON.message);
            const errs = xhr.responseJSON.error;
            let errorList = "";
            for (const key in errs) {
                if (Object.hasOwnProperty.call(errs, key)) {
                    const element = errs[key];
                    errorList += element  + "\n";
                }
            }
             alert(errorList);
        }
    });
    if(isOk == true) {
        hideAddAccountToDepartmentModal();
        hideAddNewModal();
        showAlertSuccess();
        
    }
    
    initDepartmentTable("");
}

function getDepartmentById(id){
    
    $.ajax({
        url: apiDepartmentURL + "/" + id,
        type: 'GET',
        async: false,
        success: function (result) {
            //fill data
            document.getElementById("department-id").value = result.id;
            document.getElementById("department-name").value = result.name;
            document.getElementById("department-type-select").value = result.type;
            const accounts = result.accounts;
            const checkboxes = document.getElementsByName('check-account');
            for (const account of accounts) {
                for (const checkbox of checkboxes) {
                    if (checkbox.value == account.accountId) {
                        checkbox.checked = true;
                    }
                }
            }
        }
    });
}

var departmentTemp;

// hiện modal update department
function opendUpdateDepartmentModal(id) {

    $("#add-new-department").modal("show");
    $("#add-new-department-title").html("Edit Department");
    $("#add-account-button button").hide();
    initAccountTableToAddToDepartment();

    $.ajax({
        url: apiDepartmentURL + "/" + id,
        type: 'GET',
        async: false,
        success: function (result) {
            departmentTemp = result;
            //fill data
            document.getElementById("department-id").value = result.id;
            document.getElementById("department-name").value = result.name;
            document.getElementById("department-type-select").value = result.type;
            const accounts = result.accounts;
            const checkboxes = document.getElementsByName('check-account');
            for (const account of accounts) {
                for (const checkbox of checkboxes) {
                    if (checkbox.value == account.accountId) {
                        checkbox.checked = true;
                    }
                }
            }
        }
    });


    // getDepartmentById(id);
}

// hiện modal để thêm account vào department
function showAddAccountToDepartmentModal(){
    $("#add-account-to-department").modal("show");
}

// hiện modal add account to department
function opendAddAccountToDepartmentModal(id) {
    resetDepartmentModal();
    $("#add-account-to-department").modal("show");
    $("#add-account-to-department-title").html("Add Account To Department");
    $("#add-account-button button").show();

    // khởi tạo danh sách account để add vào department
    initAccountTableToAddToDepartment();
    $.ajax({
        url: apiDepartmentURL + "/" + id,
        type: 'GET',
        async: false,
        success: function (result) {
            departmentTemp = result;
            //fill data
            document.getElementById("department-id").value = result.id;
            document.getElementById("department-name").value = result.name;
            document.getElementById("department-type-select").value = result.type;
            const accounts = result.accounts;
            const checkboxes = document.getElementsByName('check-account');
            for (const account of accounts) {
                for (const checkbox of checkboxes) {
                    if (checkbox.value == account.accountId) {
                        checkbox.checked = true;
                    }
                }
            }
        }
    });
    

    // gọi api lấy phòng ban theo id
    // getDepartmentById(id);
}


//
function openDeleteDepartmentModal(departmentID) {
    $("#delete-department-modal").modal("show");
    document.getElementById("delete-department-id").value = departmentID;
}

function hideDeleteDepartmentModal() {
    $("#delete-department-modal").modal("hide");
}

function hideDeleteDepartmentsModal() {
    $("#delete-departments-modal").modal("hide");
}


// update department
function updateDepartment(departmentID) {
    let name = document.getElementById("department-name").value;
    let type = document.getElementById("department-type-select").value;
    let accounts = getArrayID();
    let totalMember = accounts.length;

    departmentTemp.name = name;
    departmentTemp.type = type;
    departmentTemp.accounts = accounts;
    departmentTemp.totalMember = totalMember;
    
    if(departmentTemp.name.length < 1 || departmentTemp.name.length > 30){ 
        alert("Độ dài tên phòng ban không hợp lệ");
        return;
    }

    let isOk = true;

    // gọi api để cập nhật phòng ban theo id
    $.ajax({
        url: apiDepartmentURL + "/" + departmentID,
        type: 'PUT',
        data: JSON.stringify(departmentTemp), // body
        contentType: "application/json",
        async: false,
        success: function (result) {
            isOk = true;
        },
        error: function(xhr){
            isOk = false;
            console.log(xhr.responseJSON.message);
            const errs = xhr.responseJSON.error;
            let errorList = "";
            for (const key in errs) {
                if (Object.hasOwnProperty.call(errs, key)) {
                    const element = errs[key];
                    errorList += element  + "\n";
                }
            }
             alert(errorList);
        }
    });

    if(isOk == true) {
        hideAddAccountToDepartmentModal();
        hideAddNewModal();
        showAlertSuccess();

    }
    initDepartmentTable("");
}

// xóa từng department
function deleteDepartment() {
    let id = document.getElementById("delete-department-id").value;
    $.ajax({
        url: apiDepartmentURL + "/" + id,
        type: 'DELETE',
        async: false,
        success: function (result) {
        }
    });
    hideDeleteDepartmentModal();
    initDepartmentTable("");
    showAlertSuccess();
}

function openDeleteDepartmentsModal(){
    $("#delete-departments-modal").modal("show");
}

// xóa nhiều department
function deleteDepartments() {
    let ids = getArrayIdToDelete();
    let data = JSON.stringify({ids: ids});

    let isOk = true;
    $.ajax({
        url: apiDepartmentURL,
        type: 'DELETE',
        data: data,
        contentType: "application/json",
        async: false,
        success: function (result) {
            isOk = true;
        },
        error: function(xhr){
            isOk = false;
            console.log(xhr.responseJSON.message);
            const errs = xhr.responseJSON.error;
            let errorList = "";
            for (const key in errs) {
                if (Object.hasOwnProperty.call(errs, key)) {
                    const element = errs[key];
                    errorList += element  + "\n";
                }
            }
             alert(errorList);
        }
    });
    if(isOk == true) {
        hideDeleteDepartmentsModal();
        showAlertSuccess();

    }
    initDepartmentTable("");
}

// tải lại trang department 
function refreshDepartmentTable(){
    initDepartmentTable("");
}

