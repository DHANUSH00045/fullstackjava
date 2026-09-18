// Client-side dynamic search and table filtering
document.addEventListener('DOMContentLoaded', () => {
    // Dynamic table filtering
    const searchInput = document.getElementById('applicantSearchInput');
    if (searchInput) {
        searchInput.addEventListener('keyup', function () {
            const query = this.value.toLowerCase();
            const rows = document.querySelectorAll('.data-table tbody tr');
            rows.forEach(row => {
                const content = row.textContent.toLowerCase();
                row.style.display = content.includes(query) ? '' : 'none';
            });
        });
    }

    // Auto-hide alert messages
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = '0';
            alert.style.transition = 'opacity 0.5s ease';
            setTimeout(() => alert.remove(), 500);
        }, 5000);
    });
});
