// Backend API URL - change this if your backend runs on a different port
var API_URL = '';

// Show error alert
function showError(message) {
    var alert = document.getElementById('errorAlert');
    var text = document.getElementById('errorText');
    if (alert && text) {
        text.textContent = message;
        alert.classList.remove('hidden');
        // Auto-hide after 5 seconds
        setTimeout(function() {
            alert.classList.add('hidden');
        }, 5000);
    }
}

// Show success alert
function showSuccess(message) {
    var alert = document.getElementById('successAlert');
    var text = document.getElementById('successText');
    if (alert && text) {
        text.textContent = message;
        alert.classList.remove('hidden');
        // Auto-hide after 5 seconds
        setTimeout(function() {
            alert.classList.add('hidden');
        }, 5000);
    }
}

// Hide all alerts
function hideAlerts() {
    var errorAlert = document.getElementById('errorAlert');
    var successAlert = document.getElementById('successAlert');
    if (errorAlert) errorAlert.classList.add('hidden');
    if (successAlert) successAlert.classList.add('hidden');
}
