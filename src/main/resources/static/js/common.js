document.addEventListener("DOMContentLoaded", function() {
    var scriptUrls = [
        'https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js',
        'https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js',
        'https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js',
        'https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js'
    ];
    var link = document.createElement('link');
    link.rel = 'stylesheet';
    link.href = 'https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css';
    link.integrity = 'sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC';
    link.crossOrigin = 'anonymous';
    document.head.appendChild(link);

    scriptUrls.forEach(function(url) {
        var script = document.createElement('script');
        script.src = url;
        script.integrity = getScriptIntegrity(url);
        script.crossOrigin = 'anonymous';
        document.head.appendChild(script);
    });

    function getScriptIntegrity(url) {
        var integrityMap = {
            'https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js': '', // Integrity not provided by Google CDN
            'https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js': 'sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM',
            'https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js': 'sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p',
            'https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js': 'sha384-cVKIPhGWiC2Al4u+LWgxfKTRIcfu0JTxR+EQDz/bgldoEyl4H0zUF0QKbrJ0EcQF'
        };
        return integrityMap[url];
    }
});



function abc(){
    alert("hello")
}