window.addEventListener('DOMContentLoaded', () => {
    let postIdToDelete = null;

    // Define the function and attach it to the global `window` object
    window.showDeleteModal = function (postId) {
        console.log(postId);
        postIdToDelete = postId; // Save the ID of the post to delete
        const modalForDelete = document.getElementById('deleteConfirmationModal');
        const deleteModal = new bootstrap.Modal(modalForDelete);
        deleteModal.show();
    };

    // Handle the delete confirmation button
    document.getElementById('confirmDeleteButton').addEventListener('click', function () {
        if (postIdToDelete) {
            window.location.href = `/blog/deletePost/${postIdToDelete}`;
        }
    });
});
