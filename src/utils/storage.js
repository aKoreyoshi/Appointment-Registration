// author：macong
export const getItem = name => {
  const data = window.localStorage.getItem(name)
  try {
    return JSON.parse(data)
  } catch (err) {
    return data
  }
}

export const setItem = (name, value) => {
  if (typeof value === 'object') {
    value = JSON.stringify(value)
  }
  window.localStorage.setItem(name, value)
}

export const removeItem = name => {
  window.localStorage.removeItem(name)
}

// 用户信息
export const setUser = (name, value) => {
  window.localStorage.setItem(name, JSON.stringify(value))
}

export const getUser = name => {
  const user = window.localStorage.getItem(name)
  try {
    return JSON.parse(user)
  } catch (err) {
    return user
  }
}

export const removeUser = name => {
  window.localStorage.removeItem(name)
}
