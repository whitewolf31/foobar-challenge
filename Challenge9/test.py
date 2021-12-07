import math

def solution(s):
  num = int(s)
  return str(beatty(num))

def beatty(num):
  if num == 1:
    return 1
  if num == 0:
    return 0
  J = int(math.floor((math.sqrt(2) - 1) * num))
  res = num * J + num*(num+1) // 2 - J*(J+1) // 2 - beatty(J)

  return res
