export function useValidation() {
  const isEmpty = (v) => v == null || String(v).trim() === ''
  const isPositiveNumber = (v) => typeof v === 'number' && !isNaN(v) && v > 0
  const isYear = (v) => Number.isInteger(v) && v >= 1970 && v <= 2100
  const isEmail = (v) => !v || /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v)

  const validateEmployee = (form) => {
    if (isEmpty(form.name)) return { valid: false, message: '사원명을 입력해 주세요.' }
    if (isEmpty(form.employeeNumber)) return { valid: false, message: '사번을 입력해 주세요.' }
    if (!isPositiveNumber(form.baseSalary)) return { valid: false, message: '급여는 0보다 큰 숫자여야 합니다.' }
    if (!isYear(form.joinYear)) return { valid: false, message: '입사년도 형식이 올바르지 않습니다.' }
    if (isEmpty(form.position)) return { valid: false, message: '직급을 입력해 주세요.' }
    if (isEmpty(form.workStatus)) return { valid: false, message: '근무 상태를 선택해 주세요.' }
    if (isEmpty(form.workRegion)) return { valid: false, message: '근무 지역을 선택해 주세요.' }
    if (!isEmail(form.email)) return { valid: false, message: '이메일 형식이 올바르지 않습니다.' }
    return { valid: true }
  }

  const validateEvalItem = (form) => {
    if (isEmpty(form.name)) return { valid: false, message: '평가 항목명을 입력해 주세요.' }
    if (!isPositiveNumber(Number(form.maxScore))) return { valid: false, message: '배점은 0보다 큰 숫자여야 합니다.' }
    return { valid: true }
  }

  return { validateEmployee, validateEvalItem }
}


