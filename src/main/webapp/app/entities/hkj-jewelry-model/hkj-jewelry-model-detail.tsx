import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hkj-jewelry-model.reducer';

export const HkjJewelryModelDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hkjJewelryModelEntity = useAppSelector(state => state.hkjJewelryModel.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hkjJewelryModelDetailsHeading">
          <Translate contentKey="serverApp.hkjJewelryModel.detail.title">HkjJewelryModel</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hkjJewelryModelEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="serverApp.hkjJewelryModel.name">Name</Translate>
            </span>
          </dt>
          <dd>{hkjJewelryModelEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="serverApp.hkjJewelryModel.description">Description</Translate>
            </span>
          </dt>
          <dd>{hkjJewelryModelEntity.description}</dd>
          <dt>
            <span id="isCustom">
              <Translate contentKey="serverApp.hkjJewelryModel.isCustom">Is Custom</Translate>
            </span>
          </dt>
          <dd>{hkjJewelryModelEntity.isCustom ? 'true' : 'false'}</dd>
          <dt>
            <span id="weight">
              <Translate contentKey="serverApp.hkjJewelryModel.weight">Weight</Translate>
            </span>
          </dt>
          <dd>{hkjJewelryModelEntity.weight}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="serverApp.hkjJewelryModel.price">Price</Translate>
            </span>
          </dt>
          <dd>{hkjJewelryModelEntity.price}</dd>
          <dt>
            <span id="color">
              <Translate contentKey="serverApp.hkjJewelryModel.color">Color</Translate>
            </span>
          </dt>
          <dd>{hkjJewelryModelEntity.color}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="serverApp.hkjJewelryModel.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{hkjJewelryModelEntity.notes}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="serverApp.hkjJewelryModel.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{hkjJewelryModelEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="serverApp.hkjJewelryModel.active">Active</Translate>
            </span>
          </dt>
          <dd>{hkjJewelryModelEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="serverApp.hkjJewelryModel.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{hkjJewelryModelEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="serverApp.hkjJewelryModel.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjJewelryModelEntity.createdDate ? (
              <TextFormat value={hkjJewelryModelEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="serverApp.hkjJewelryModel.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{hkjJewelryModelEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="serverApp.hkjJewelryModel.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjJewelryModelEntity.lastModifiedDate ? (
              <TextFormat value={hkjJewelryModelEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="serverApp.hkjJewelryModel.project">Project</Translate>
          </dt>
          <dd>{hkjJewelryModelEntity.project ? hkjJewelryModelEntity.project.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/hkj-jewelry-model" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hkj-jewelry-model/${hkjJewelryModelEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HkjJewelryModelDetail;
