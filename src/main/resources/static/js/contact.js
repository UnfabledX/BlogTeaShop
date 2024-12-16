document.addEventListener("DOMContentLoaded", function () {
    const successToast = document.getElementById('successToast');
    showToast(successToast);
    const errorToast = document.getElementById('errorToast');
    showToast(errorToast);

    function showToast(toastElement) {
        if (toastElement) {
            const toast = new bootstrap.Toast(toastElement);
            toast.show();
        }
    }
});

