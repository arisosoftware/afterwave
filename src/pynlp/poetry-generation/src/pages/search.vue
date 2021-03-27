<template>
  <div id="search" v-loading="loading">
    <div class="search-background">
      <el-image
        class="background-image"
        :src="BackgroundImage"
        fit="cover"></el-image>
    </div>

    <div class="container">
      <div class="content">
        <el-row class="search-panel">
          <el-row class="team-name">
            <h1>Poetry Generation</h1>
          </el-row>
          <el-row class="search-input">
            <el-input v-model="keyword" placeholder="请输入内容"></el-input>
            <el-button @click="poetryGeneration" type="text" class="search-button">
              <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M 13.125 0 C 13.7565 0 14.3652 0.0813802 14.9512 0.244141 C 15.5371 0.406901 16.084 0.638021 16.5918 0.9375 C 17.0996 1.23698 17.5618 1.59831 17.9785 2.02148 C 18.4017 2.43815 18.763 2.90039 19.0625 3.4082 C 19.362 3.91602 19.5931 4.46289 19.7559 5.04883 C 19.9186 5.63477 20 6.24349 20 6.875 C 20 7.50651 19.9186 8.11523 19.7559 8.70117 C 19.5931 9.28711 19.362 9.83398 19.0625 10.3418 C 18.763 10.8496 18.4017 11.3151 17.9785 11.7383 C 17.5618 12.1549 17.0996 12.513 16.5918 12.8125 C 16.084 13.112 15.5371 13.3431 14.9512 13.5059 C 14.3652 13.6686 13.7565 13.75 13.125 13.75 C 12.3112 13.75 11.5299 13.6133 10.7812 13.3398 C 10.0391 13.0664 9.35547 12.6725 8.73047 12.1582 L 1.06445 19.8145 C 0.940755 19.9382 0.794271 20 0.625 20 C 0.455729 20 0.309245 19.9382 0.185547 19.8145 C 0.061849 19.6908 0 19.5443 0 19.375 C 0 19.2057 0.061849 19.0592 0.185547 18.9355 L 7.8418 11.2695 C 7.32747 10.6445 6.93359 9.96094 6.66016 9.21875 C 6.38672 8.47005 6.25 7.6888 6.25 6.875 C 6.25 6.24349 6.33138 5.63477 6.49414 5.04883 C 6.6569 4.46289 6.88802 3.91602 7.1875 3.4082 C 7.48698 2.90039 7.84505 2.43815 8.26172 2.02148 C 8.6849 1.59831 9.15039 1.23698 9.6582 0.9375 C 10.166 0.638021 10.7129 0.406901 11.2988 0.244141 C 11.8848 0.0813802 12.4935 0 13.125 0 Z M 13.125 12.5 C 13.8997 12.5 14.6289 12.3535 15.3125 12.0605 C 15.9961 11.7611 16.5918 11.3574 17.0996 10.8496 C 17.6074 10.3418 18.0078 9.74609 18.3008 9.0625 C 18.6003 8.37891 18.75 7.64974 18.75 6.875 C 18.75 6.10026 18.6003 5.37109 18.3008 4.6875 C 18.0078 4.00391 17.6074 3.4082 17.0996 2.90039 C 16.5918 2.39258 15.9961 1.99219 15.3125 1.69922 C 14.6289 1.39974 13.8997 1.25 13.125 1.25 C 12.3503 1.25 11.6211 1.39974 10.9375 1.69922 C 10.2539 1.99219 9.6582 2.39258 9.15039 2.90039 C 8.64258 3.4082 8.23893 4.00391 7.93945 4.6875 C 7.64648 5.37109 7.5 6.10026 7.5 6.875 C 7.5 7.64974 7.64648 8.37891 7.93945 9.0625 C 8.23893 9.74609 8.64258 10.3418 9.15039 10.8496 C 9.6582 11.3574 10.2539 11.7611 10.9375 12.0605 C 11.6211 12.3535 12.3503 12.5 13.125 12.5 Z" fill="#924534"></path></svg>
            </el-button>
          </el-row>
          <el-row class="generation-types">
            <el-radio-group v-model="selectedType" size="medium" style="margin-right:10px;">
              <el-radio-button
                v-for="(itemType, index) in generationTypes"
                :key="index"
                :label="itemType.name"
              ></el-radio-button>
            </el-radio-group>
            <el-radio-group v-model="selectedGeType" size="medium">
              <el-radio-button
                v-for="(itemType, index) in selectedGeTypes"
                :key="index"
                :label="itemType.name"
              ></el-radio-button>
            </el-radio-group>
          </el-row>
          <!-- <el-row class="generation-types">
            <el-radio-group v-model="selectedModelType" size="medium">
              <el-radio-button
                v-for="(itemType, index) in selectedModelTypes"
                :key="index"
                :label="itemType.name"
              ></el-radio-button>
            </el-radio-group>
          </el-row> -->
        </el-row>
        <el-row class="results" v-if="showResults">
          <el-col
            v-for="(result, index) in results"
            :key="index"
            :span="6"
            class="result-block">
            <el-row class="title">
              <p>{{result.model}}</p>
            </el-row>
            <el-row class="result-item">
              <p style="white-space: pre-wrap;" v-html="result.content">
              </p>
            </el-row>
          </el-col>
        </el-row>
      </div>
      <div class="web-footer">
        <p v-html="'©&nbsp;&nbsp;2020.12&nbsp;&nbsp;by&nbsp;&nbsp;非必要不出校队'"></p>
      </div>
    </div>
  </div>
</template>

<script>
import { getPoetry, getPoetryLSTM } from '@/api/index';
import BackgroundImage from '@/assets/background.jpg';
export default {
  name: 'PoetryGeneration',
  data() {
    return {
      BackgroundImage,
      loading: false,
      showResults: true,
      selectedType: '五言绝句',
      keyword: '',
      results: [
        // {
        //   model: 'lstm',
        //   content: '白日依山尽，\n黄河入海流。'
        // }, {
        //   model: 'lstm',
        //   content: '白日依山尽'
        // }
      ],
      selectedGeType: '随机',
      selectedGeTypes: [{
        name: '随机',
        value: '随机'
      }, {
        name: '藏头诗',
        value: '藏头诗'
      }, {
        name: '开头诗',
        value: '开头诗'
      }, {
        name: '主题',
        value: '主题'
      }],
      // selectedModelType: 'lstm',
      selectedModelTypes: [{
        name: 'roberta',
        value: 'roberta'
      }, {
        name: 'bert',
        value: 'bert'
      }, {
        name: 'GPT2',
        value: 'GPT2'
      }, {
        name: 'LSTM',
        value: 'LSTM'
      }],
      generationTypes: [{
        name: '五言绝句',
        value: '五言绝句'
      }, {
        name: '七言绝句',
        value: '七言绝句'
      }, {
        name: '五言律诗',
        value: '五言律诗'
      }, {
        name: '七言律诗',
        value: '七言律诗'
      }],
    };
  },
  methods: {
    async poetryGeneration() {
      this.results = [];
      this.loading = true;
      let func = [];
      for (let type of this.selectedModelTypes) {
        func.push(this.generatePoemOne(type.name))
      }
      Promise.all(func)
      .then(() => {
        this.loading = false;
      });
    },
    async generatePoemOne(selectedModelType) {
      let getPoetryAll = getPoetry;
      if ((this.selectedGeType === '主题' || this.selectedType === '七言律诗') && (selectedModelType === 'bert' || selectedModelType === 'roberta')) {
        return;
      }
      if ((this.selectedType === '五言律诗' || this.selectedType === '七言律诗') && selectedModelType === 'GPT2') {
        return;
      }
      if (selectedModelType === 'LSTM' || selectedModelType === 'GPT2') {
        getPoetryAll = getPoetryLSTM;
      }
      const res = await getPoetryAll({
        keyword: this.keyword,
        generationType: this.selectedGeType,
        model: selectedModelType,
        poetryType: this.selectedType
      });
      if (res.status == 200 && res.data.poetry) {
        this.showResults = true;
        const str = res.data.poetry;
        let content = "";
        for (const ch of str) {
          content += ch;
          if (ch === '，' || ch === '。' || ch === '？') {
            content += '\n';
          }
        }
        
        this.results.push({
          model: selectedModelType,
          content
        });
      }
      return;
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
#search {
  position: relative;
  height: 100%;
}

.container {
  display: flex;
  flex-direction: column;
  height: 100%;
  justify-content: space-between;
}

.content {
  display: flex;
  flex-direction: column;
}

.web-footer {
  color: rgba(255, 255, 255, 0.8);
  font-size: 10px;
  margin-top: 50px;
}

.team-name {
  color: #fff;
  margin-bottom: 2%;
  /* font-size: 20px; */
}

.search-background {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: -1;
}

.background-image {
  width: 100%;
  height: 100%;
  /* opacity: 0.85; */
}

.search-panel {
  display: flex;
  align-items: center;
  flex-direction: column;
  margin-top: 10%;
}

.search-input {
  width: 50%;
  position: relative;
}

.search-input input {
  padding-left: 10px;
  padding-right: calc(9% + 10px);
  height: 44px;
  line-height: 44px;
}

.search-button {
  padding: 0;
  margin: 0;
  border: none;
  position: absolute;
  height: 44px;
  width: 9%;
  right: 0;
}

.generation-types {
  /* width: 50%; */
  margin-top: 10px;
}

.generation-types .el-radio-group {
  float: left;
}

.generation-types .el-radio-button__inner {
  background-color: rgba(255, 255, 255, 0.8);
}

.results {
  width: 80%;
  align-self: center;
  margin-top: 2%;
  display: flex;
  justify-content: center;
}

.result-item {
  background-color: rgba(255, 255, 255, 0.75);
  border-radius: 5px;
  margin: 5px 0;
}

.result-item:hover {
  background-color: rgba(255, 255, 255, 0.85);
}

.title {
  color: #fff;
  display: flex;
}

.result-item {
  margin-right: 10px;
}
</style>
