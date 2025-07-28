const express = require('express');
const cors = require('cors');
const ciclistaRoutes = require('./controllers/ciclistaController');
const aluguelRoutes = require('./controllers/aluguelController');
const funcionarioRoutes = require('./controllers/funcionarioController'); 
const cartaoRoutes = require('./controllers/cartaoController');
const adminRoutes = require('./controllers/adminController');



const app = express();
app.use(express.json());
app.use(cors());

app.use('/', ciclistaRoutes);
app.use('/', aluguelRoutes);
app.use('/', funcionarioRoutes); 
app.use('/', cartaoRoutes);
app.use('/', adminRoutes);

app.listen(3000, () => {
    console.log('Servidor rodando na porta 3000');
});

module.exports = app;
