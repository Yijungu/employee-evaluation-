export function toast(message, variant = 'primary') {
  const el = document.createElement('div')
  el.className = `position-fixed top-0 end-0 p-3`
  el.style.zIndex = 1080
  el.innerHTML = `
    <div class="toast align-items-center text-bg-${variant} border-0 show" role="alert" aria-live="assertive" aria-atomic="true">
      <div class="d-flex">
        <div class="toast-body">${message}</div>
        <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
      </div>
    </div>`
  document.body.appendChild(el)
  setTimeout(() => el.remove(), 2500)
}


