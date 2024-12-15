
window.addEventListener('DOMContentLoaded', () => {
    const olderPosts = document.getElementById('older-posts');
    if (olderPosts !== null) {
        olderPosts.onclick = async function () {
            try {
                const response = await fetch('/blog/loadMorePosts', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' }
                });
                if (response.ok) {
                    const fragment = await response.text();
                    console.log(fragment);
                    const listOfPosts = document.getElementById('list-of-posts');
                    listOfPosts.outerHTML = fragment;
                } else {
                    console.error('Failed to load posts:', response.status, response.statusText);
                }
            } catch (error) {
                console.error('An error occurred:', error);
            }
        };
    }
})
