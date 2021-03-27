import axios from 'axios';

async function getPoetry(data) {
  const res = await axios({
    method: 'get',
    url: `/v1/get-poetry?model=${data.model}&generationType=${data.generationType}&keyword=${data.keyword}&poetryType=${data.poetryType}`
  });

  return res;
}

async function getPoetryLSTM(data) {
  const res = await axios({
    method: 'get',
    url: `/v2/get-poetry?model=${data.model}&generationType=${data.generationType}&keyword=${data.keyword}&poetryType=${data.poetryType}`
  });

  return res;
}

export {
    getPoetry,
    getPoetryLSTM
};
