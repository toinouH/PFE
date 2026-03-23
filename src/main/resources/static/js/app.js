/**
 * Gestion du loader d'enrichissement avec animation de carte
 */

document.addEventListener('DOMContentLoaded', function () {
    const enrichirForm = document.getElementById('enrichirForm');
    const loaderModal = document.getElementById('loaderModal');
    const timerMinutes = document.getElementById('timerMinutes');
    const timerSeconds = document.getElementById('timerSeconds');

    if (!enrichirForm) return;

    enrichirForm.addEventListener('submit', function (e) {
        e.preventDefault();
        
        // Afficher le loader
        showLoader();
        
        // Temps de démarrage
        const startTime = Date.now();
        const minimumDuration = 5000; // 5 secondes en millisecondes

        // Préparer les données du formulaire
        const formData = new FormData(enrichirForm);
        const searchParams = new URLSearchParams(formData);

        // Lancer le traitement d'enrichissement
        fetch(enrichirForm.action, {
            method: 'POST',
            body: searchParams,
            headers: {
                'X-Requested-With': 'XMLHttpRequest',
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Erreur réseau ou serveur (' + response.status + ')');
                }
                // Vérifier le temps écoulé
                const elapsedTime = Date.now() - startTime;
                const remainingTime = Math.max(0, minimumDuration - elapsedTime);

                // Attendre le temps minimum restant
                return new Promise(resolve => {
                    setTimeout(resolve, remainingTime);
                });
            })
            .then(() => {
                // Afficher le message de succès dans le loader (si une fonction showSuccessMessage existe)
                if (typeof showSuccessMessage === 'function') {
                    showSuccessMessage();
                } else {
                    // Fallback si la fonction n'est pas définie
                    const loaderTitle = loaderModal.querySelector('.loader-title');
                    loaderTitle.innerHTML = '<i class="fas fa-check-circle"></i> Traitement terminé';
                    loaderTitle.style.color = '#28a745';
                }

                // Attendre 2 secondes pour que l'utilisateur voie le message
                return new Promise(resolve => setTimeout(resolve, 2000));
            })
            .then(() => {
                // Redirection vers la page des fichiers (utiliser l'action réelle du formulaire)
                globalThis.location.href = '/fichiers';
            })
            .catch(error => {
                console.error('Erreur lors de l\'enrichissement:', error);
                showErrorMessage(error.message || 'Une erreur inconnue s\'est produite');
            });
    });

    function showLoader() {
        loaderModal.classList.add('active');
        startTimer();
    }

    function hideLoader() {
        loaderModal.classList.remove('active');
        stopTimer();
    }

    function showErrorMessage(errorMessage) {
        const loaderContent = loaderModal.querySelector('.loader-content');
        const loaderTitle = loaderModal.querySelector('.loader-title');
        const mapContainer = loaderModal.querySelector('.map-container');
        const loaderText = loaderModal.querySelector('.loader-text');
        const loaderBar = loaderModal.querySelector('.loader-bar');

        // Changer le titre
        loaderTitle.innerHTML = '<i class="fas fa-exclamation-triangle"></i> Erreur de traitement';
        loaderTitle.style.color = '#e60028';

        // Remplacer le contenu de la map par un message d'erreur
        mapContainer.innerHTML = `
            <div class="error-animation">
                <div class="error-icon">
                    <i class="fas fa-times"></i>
                </div>
                <div class="error-text">
                    <h3>Échec de l'enrichissement</h3>
                    <p>${errorMessage}</p>
                </div>
            </div>
        `;

        // Mettre à jour le texte
        loaderText.innerHTML = `
            <p>Veuillez réessayer ou contacter le support.</p>
            <button onclick="hideLoader()" class="btn-rail" style="background: #e60028; margin-top: 15px;">Fermer</button>
        `;

        // Masquer la barre de progression
        loaderBar.style.display = 'none';

        // Arrêter le timer
        stopTimer();
    }

    // === TIMER COUNTDOWN ===
    let timerInterval;

    function startTimer() {
        // 10 secondes de timer minimum
        let remainingSeconds = 5;
        updateTimerDisplay(remainingSeconds);
        
        timerInterval = setInterval(() => {
            remainingSeconds--;
            updateTimerDisplay(remainingSeconds);
            
            if (remainingSeconds <= 0) {
                stopTimer();
            }
        }, 1000);
    }

    function stopTimer() {
        if (timerInterval) {
            clearInterval(timerInterval);
        }
    }

    function updateTimerDisplay(seconds) {
        const minutes = Math.floor(seconds / 60);
        const secs = seconds % 60;
        
        timerMinutes.textContent = minutes;
        timerSeconds.textContent = secs.toString().padStart(2, '0');
    }

    // Rendre hideLoader accessible globalement pour le bouton d'erreur
    globalThis.hideLoader = hideLoader;
});
